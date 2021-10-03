package com.wesleydonk.update.internal.controller

import com.wesleydonk.update.*
import com.wesleydonk.update.internal.managers.SystemDownloadManager

interface Controller {
    suspend fun execute()
}

class DefaultController(
    private val fetcher: Fetcher,
    private val parser: Parser,
    private val storage: Storage,
    private val systemDownloadManager: SystemDownloadManager,
) : Controller {

    override suspend fun execute() {
        deleteAll()
        val result = fetcher.latestVersionResult()
        if (result != null) {
            storeUpdate(result)
        }
    }

    private suspend fun deleteAll() {
        storage.get()?.downloadId?.let { downloadId ->
            systemDownloadManager.delete(downloadId)
        }
        storage.deleteAll()
    }

    private suspend fun storeUpdate(update: CheckVersionResult) {
        val version = parser.parse(update)
        storage.insert(version)
    }

    companion object {
        fun ofConfig(config: UpdateConfig): DefaultController = with(config) {
            return DefaultController(fetcher, parser, storage, systemDownloadManager)
        }
    }
}