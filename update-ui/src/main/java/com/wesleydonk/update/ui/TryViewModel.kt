package com.wesleydonk.update.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.wesleydonk.update.DownloadResult
import com.wesleydonk.update.Storage
import com.wesleydonk.update.Version
import com.wesleydonk.update.internal.managers.SystemDownloadManager
import com.wesleydonk.update.internal.managers.SystemDownloadManagerImpl
import com.wesleydonk.update.ui.internal.DownloadStatus
import com.wesleydonk.update.ui.internal.extensions.Event
import com.wesleydonk.update.ui.internal.managers.FileManager
import com.wesleydonk.update.ui.internal.managers.FileManagerImpl
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

internal class TryViewModel(
    private val storage: Storage,
    private val systemDownloadManager: SystemDownloadManager,
    private val fileManager: FileManager
) : ViewModel() {

    val installApk = MutableLiveData<Event<Pair<String, String>>>()

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

            val filePath = fileManager.createFile(version.version)
            val downloadId = systemDownloadManager.download(version, filePath)
            downloadIdData.value = downloadId
        }
    }

    private fun isDownloadInProgress(): Boolean = downloadIdData.value != null

    private fun DownloadResult.asStatus(): DownloadStatus = when (this) {
        is DownloadResult.Completed -> {
            installApk.postValue(Event(this.filePath to this.fileMimeType))
            DownloadStatus(null)
        }
        DownloadResult.Failed -> DownloadStatus(null)
        is DownloadResult.InProgress -> DownloadStatus(downloadPercentage = percentage)
    }

    class Factory(
        private val storage: Storage,
        private val context: Context,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TryViewModel::class.java)) {
                val downloadManager = SystemDownloadManagerImpl(context)
                val fileManager = FileManagerImpl(context)
                @Suppress("UNCHECKED_CAST")
                return TryViewModel(storage, downloadManager, fileManager) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}