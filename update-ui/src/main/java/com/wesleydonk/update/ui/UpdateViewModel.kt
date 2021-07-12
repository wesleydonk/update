package com.wesleydonk.update.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.wesleydonk.update.DownloadResult
import com.wesleydonk.update.Update
import com.wesleydonk.update.Version
import com.wesleydonk.update.internal.managers.SystemDownloadManager
import com.wesleydonk.update.internal.managers.SystemDownloadManagerImpl
import com.wesleydonk.update.ui.internal.InstallApk
import com.wesleydonk.update.ui.internal.DownloadStatus
import com.wesleydonk.update.ui.internal.extensions.Event
import com.wesleydonk.update.ui.internal.managers.FileManager
import com.wesleydonk.update.ui.internal.managers.FileManagerImpl
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

internal class UpdateViewModel(
    private val update: Update,
    private val systemDownloadManager: SystemDownloadManager,
    private val fileManager: FileManager
) : ViewModel() {

    private val installApkData = MutableLiveData<Event<InstallApk>>()
    val installApk: LiveData<Event<InstallApk>> = installApkData

    private val downloadIdData = MutableLiveData<Long?>()
    private val downloadStatusData = downloadIdData.switchMap { id ->
        id?.let {
            systemDownloadManager.observe(id)
                .map(::transformToStatus)
                .asLiveData()
        } ?: MutableLiveData(DownloadStatus())
    }
    val downloadStatus: LiveData<DownloadStatus> = downloadStatusData

    private lateinit var version: Version

    init {
        viewModelScope.launch {
            version = update.getStoredVersion()
                ?: throw IllegalStateException("UI can only be started when new version is available")
        }
    }

    fun startDownload() {
        viewModelScope.launch {
            if (isDownloadInProgress()) {
                Log.i("Try", "Download was already started, extra check for safety")
                return@launch
            }

            val filePath = fileManager.createFile(version.version)
            val downloadId = systemDownloadManager.download(version, filePath)
            downloadIdData.value = downloadId
        }
    }

    private fun isDownloadInProgress(): Boolean =
        downloadIdData.value != null && downloadStatusData.value?.downloadPercentage != null

    private fun transformToStatus(result: DownloadResult): DownloadStatus {
        // side effect for triggering the install mechanism
        if (result is DownloadResult.Completed) {
            val installApk = InstallApk(result.filePath, result.fileMimeType)
            installApkData.postValue(Event(installApk))
        }

        return when (result) {
            is DownloadResult.Completed -> DownloadStatus(null)
            DownloadResult.Failed -> DownloadStatus(null)
            is DownloadResult.InProgress -> DownloadStatus(downloadPercentage = result.percentage)
        }
    }

    class Factory(
        private val context: Context,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UpdateViewModel::class.java)) {
                val update = Update.getInstance()
                val downloadManager = SystemDownloadManagerImpl(context)
                val fileManager = FileManagerImpl(context)
                @Suppress("UNCHECKED_CAST")
                return UpdateViewModel(update, downloadManager, fileManager) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}