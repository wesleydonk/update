package com.wesleydonk.update.internal.managers

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.getSystemService
import com.wesleydonk.update.DownloadResult
import com.wesleydonk.update.Version
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.roundToInt

interface SystemDownloadManager {
    suspend fun download(version: Version): Long
    fun delete(downloadId: Long)
    fun observe(downloadId: Long): Flow<DownloadResult>
}

class SystemDownloadManagerImpl(
    private val context: Context,
) : SystemDownloadManager {

    private val downloadManager: DownloadManager by lazy {
        context.getSystemService()!!
    }

    override suspend fun download(version: Version): Long {
        val url = Uri.parse(version.downloadUrl)
        val request = DownloadManager.Request(url).apply {
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "update-${version.version}.apk"
            )
            setTitle("Download of new version (${version.version})")
        }

        return downloadManager.enqueue(request)
    }

    override fun delete(downloadId: Long) {
        downloadManager.remove(downloadId)
    }

    override fun observe(downloadId: Long): Flow<DownloadResult> {
        val query = DownloadManager.Query().setFilterById(downloadId)

        var isDownloading = true
        return flow {
            while (isDownloading) {
                downloadManager.query(query).use { cursor ->
                    if (cursor.moveToFirst()) {
                        val downloadBytes = cursor.getInt(
                            cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                        )
                        val totalBytes = cursor.getInt(
                            cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                        )
                        val status =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        val filePath =
                            downloadManager.getUriForDownloadedFile(downloadId).toString()

                        transformDownload(
                            downloadBytes,
                            totalBytes,
                            status,
                            filePath
                        )?.let { (result, isCompleted) ->
                            emit(result)
                            isDownloading = !isCompleted
                        }
                    }
                }
            }
        }
    }

    private fun transformDownload(
        downloadBytes: Int,
        totalBytes: Int,
        status: Int,
        filePath: String
    ): Pair<DownloadResult, Boolean>? = when (status) {
        DownloadManager.STATUS_FAILED -> DownloadResult.Failed to true
        DownloadManager.STATUS_PENDING,
        DownloadManager.STATUS_RUNNING -> DownloadResult.InProgress(
            percentageOf(
                downloadBytes,
                totalBytes
            )
        ) to false
        DownloadManager.STATUS_SUCCESSFUL -> DownloadResult.Completed(filePath) to true
        else -> null
    }

    private fun percentageOf(value: Int, total: Int): Int {
        return ((value.toFloat() / total.toFloat()) * 100F).roundToInt()
    }
}