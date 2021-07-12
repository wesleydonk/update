package com.wesleydonk.update.internal.controller

import android.content.Context
import com.wesleydonk.update.*
import com.wesleydonk.update.Storage
import com.wesleydonk.update.internal.managers.SystemDownloadManager
import com.wesleydonk.update.internal.managers.SystemDownloadManagerImpl

interface Controller {
    suspend fun execute()
}

class DefaultController(
    context: Context,
    private val fetcher: Fetcher,
    private val parser: Parser,
    private val storage: Storage,
    private val systemDownloadManager: SystemDownloadManager = SystemDownloadManagerImpl(context),
) : Controller {
    override suspend fun execute() {
        deleteAll()
        when (val result = fetcher.latestVersionResult()) {
            is CheckVersionResult.NewUpdate -> storeUpdate(result)
            CheckVersionResult.NoUpdate -> Unit
        }
    }

    private suspend fun deleteAll() {
        storage.get()?.downloadId?.let { downloadId ->
            systemDownloadManager.delete(downloadId)
        }
        storage.deleteAll()
    }

    private suspend fun storeUpdate(update: CheckVersionResult.NewUpdate) {
        val version = parser.parse(update)
        storage.insert(version)
    }

    companion object {
        fun ofConfig(config: TryConfig): DefaultController {
            return DefaultController(
                config.context,
                config.fetcher,
                config.parser,
                config.storage,
            )
        }
    }
}