package com.wesleydonk.update.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.wesleydonk.update.DownloadResult
import com.wesleydonk.update.Storage
import com.wesleydonk.update.Version
import com.wesleydonk.update.internal.managers.SystemDownloadManager
import com.wesleydonk.update.internal.managers.SystemDownloadManagerImpl
import com.wesleydonk.update.internal.storage.RoomStorage
import com.wesleydonk.update.ui.internal.DownloadStatus
import com.wesleydonk.update.ui.internal.InstallableFile
import com.wesleydonk.update.ui.internal.extensions.Event
import com.wesleydonk.update.ui.internal.managers.FileManager
import com.wesleydonk.update.ui.internal.managers.FileManagerImpl
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val EXTRAS_VERSION = "EXTRAS_VERSION"

internal class UpdateViewModel(
    private val storage: Storage,
    private val systemDownloadManager: SystemDownloadManager,
    private val fileManager: FileManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val installableFileData = MutableLiveData<Event<InstallableFile>>()
    val installableFile: LiveData<Event<InstallableFile>> = installableFileData

    private val downloadIdData = MutableLiveData<Long?>()
    private val downloadStatusData = downloadIdData.switchMap { id ->
        id?.let {
            systemDownloadManager.observe(id)
                .map(::transformToStatus)
                .asLiveData()
        } ?: MutableLiveData(DownloadStatus())
    }
    val downloadStatus: LiveData<DownloadStatus> = downloadStatusData

    private var version: Version?
        get() = savedStateHandle.get(EXTRAS_VERSION)
        set(value) {
            savedStateHandle[EXTRAS_VERSION] = value
        }

    fun startDownload() {
        viewModelScope.launch {
            val version = version ?: storage.get().also {
                this@UpdateViewModel.version = it
            }

            if (version == null || isDownloadInProgress()) {
                Log.i("Try", "Download was already started, extra check for safety")
                return@launch
            }

            val filePath = fileManager.createFile(version.id)
            val downloadId = systemDownloadManager.download(version, filePath)
            storage.insert(version.copy(downloadId = downloadId))

            downloadIdData.value = downloadId
        }
    }

    private fun isDownloadInProgress(): Boolean =
        downloadIdData.value != null && downloadStatusData.value?.downloadPercentage != null

    private fun transformToStatus(result: DownloadResult): DownloadStatus {
        // side effect for triggering the install mechanism
        if (result is DownloadResult.Completed) {
            val installApk = InstallableFile(result.filePath, result.fileMimeType)
            installableFileData.postValue(Event(installApk))
        }

        return when (result) {
            is DownloadResult.Completed -> DownloadStatus(downloadPercentage = null)
            is DownloadResult.InProgress -> DownloadStatus(downloadPercentage = result.percentage)
            DownloadResult.Failed -> DownloadStatus(downloadPercentage = null)
        }
    }

    class Factory(
        private val context: Context,
        owner: SavedStateRegistryOwner,
    ) : AbstractSavedStateViewModelFactory(owner, null) {
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            if (modelClass.isAssignableFrom(UpdateViewModel::class.java)) {
                val storage = RoomStorage(context)
                val downloadManager = SystemDownloadManagerImpl(context)
                val fileManager = FileManagerImpl(context)
                @Suppress("UNCHECKED_CAST")
                return UpdateViewModel(storage, downloadManager, fileManager, handle) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
