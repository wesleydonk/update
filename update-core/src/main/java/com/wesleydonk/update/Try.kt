package com.wesleydonk.update

import android.content.Context
import com.wesleydonk.update.internal.controller.DefaultController
import com.wesleydonk.update.internal.managers.SystemDownloadManagerImpl
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow

class Try(
    private val config: TryConfig
) {

    private val controller = DefaultController.ofConfig(config)
    private val systemDownloadManager = SystemDownloadManagerImpl(config.context)

    suspend fun checkVersion(): Version? {
        controller.execute()
        return config.storage.get()
    }

    suspend fun getStoredVersion(): Version? {
        return config.storage.get()
    }

    suspend fun downloadVersion(version: Version): Flow<DownloadResult> {
        val downloadId = systemDownloadManager.download(version)
        return systemDownloadManager.observe(downloadId)
    }

    class Builder(
        private val config: TryConfig.Builder
    ) {
        fun build(context: Context): Try {
            return Try(config.build(context))
        }
    }
}