package com.wesleydonk.update.internal.managers

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import com.wesleydonk.update.DownloadResult
import com.wesleydonk.update.Version
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.math.roundToInt

private const val DOWNLOAD_URL_FORMAT = "file://%s"

interface SystemDownloadManager {
    suspend fun download(version: Version, filePath: String): Long
    fun delete(downloadId: Long)
    fun observe(downloadId: Long): Flow<DownloadResult>
}

class SystemDownloadManagerImpl(
    private val context: Context,
) : SystemDownloadManager {

    private var isDownloading = false
    private var filePath: String = ""

    private val downloadManager: DownloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    override suspend fun download(version: Version, filePath: String): Long {
        this.filePath = filePath
        val url = Uri.parse(version.downloadUrl)
        val request = DownloadManager.Request(url).apply {
            setTitle("Download of new version (${version.id})")

            val fileUri = DOWNLOAD_URL_FORMAT.format(filePath).toUri()
            setDestinationUri(fileUri)
        }

        return downloadManager.enqueue(request).also {
            isDownloading = true
        }
    }

    override fun delete(downloadId: Long) {
        downloadManager.remove(downloadId)
    }

    override fun observe(downloadId: Long): Flow<DownloadResult> = flow {
        val query = DownloadManager.Query()
            .setFilterById(downloadId)

        while (isDownloading) {
            val cursor = downloadManager.query(query)
            if (cursor == null || !cursor.moveToFirst()) {
                emit(DownloadResult.Failed)
                return@flow
            }

            cursor.use {

                val filePath = filePath
                val downloadedBytes =
                    cursor.getColumnInt(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                val totalBytes = cursor.getColumnInt(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                val status = cursor.getColumnInt(DownloadManager.COLUMN_STATUS)
                val isCompleted = cursor.isCompleted()

                val result = transformDownload(
                    downloadedBytes,
                    totalBytes,
                    status,
                    filePath,
                    downloadId,
                )

                if (isCompleted) {
                    isDownloading = false
                    emit(result)
                    return@flow
                }

                emit(result)
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun transformDownload(
        downloadedBytes: Int,
        totalBytes: Int,
        status: Int,
        filePath: String,
        id: Long,
    ): DownloadResult = when (status) {
        DownloadManager.STATUS_FAILED -> DownloadResult.Failed
        DownloadManager.STATUS_SUCCESSFUL -> {
            val fileMimeType = downloadManager.getMimeTypeForDownloadedFile(id)
            DownloadResult.Completed(filePath, fileMimeType)
        }
        else -> {
            val percentage = percentageOf(downloadedBytes, totalBytes)
            DownloadResult.InProgress(percentage)
        }
    }

    private fun percentageOf(value: Int, total: Int): Int {
        return ((value.toFloat() / total.toFloat()) * 100F).roundToInt()
    }
}

private fun Cursor.isCompleted(): Boolean =
    getInt(getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL

private fun Cursor.getColumnInt(columnName: String) = getInt(getColumnIndex(columnName))