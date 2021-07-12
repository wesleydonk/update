package com.wesleydonk.update.ui

import android.util.Log
import androidx.lifecycle.*
import com.wesleydonk.update.DownloadResult
import com.wesleydonk.update.Storage
import com.wesleydonk.update.Version
import com.wesleydonk.update.internal.managers.SystemDownloadManager
import com.wesleydonk.update.ui.internal.DownloadStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

internal class TryViewModel(
    private val storage: Storage,
    private val systemDownloadManager: SystemDownloadManager
) : ViewModel() {

    private val downloadIdData = MutableLiveData<Long?>()
    private val downloadStatusData = downloadIdData.switchMap { id ->
        id?.let {
            systemDownloadManager.observe(id).map { result -> result.asStatus() }.asLiveData()
        } ?: MutableLiveData(DownloadStatus())
    }
    val downloadStatus: LiveData<DownloadStatus> = downloadStatusData

    private lateinit var version: Version

    init {
        viewModelScope.launch {
            version = storage.get()
                ?: throw IllegalStateException("UI can only be started when new version is available")
        }
    }

    fun startDownload() {
        viewModelScope.launch {
            if (isDownloadInProgress()) {
                Log.i("Try", "Download was already started, extra check for safety")
                return@launch
            }

            val downloadId = systemDownloadManager.download(version)
            downloadIdData.value = downloadId
        }
    }

    private fun isDownloadInProgress(): Boolean = downloadIdData.value != null

    private fun DownloadResult.asStatus(): DownloadStatus = when (this) {
        is DownloadResult.Completed -> DownloadStatus(null) // TODO fire event to install DownloadStatus.(this.filePath)
        DownloadResult.Failed -> DownloadStatus(null)
        is DownloadResult.InProgress -> DownloadStatus(downloadPercentage = percentage)
    }
}