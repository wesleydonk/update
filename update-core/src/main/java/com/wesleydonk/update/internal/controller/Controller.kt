package com.wesleydonk.update.internal.controller

import com.wesleydonk.update.DataStore
import com.wesleydonk.update.Fetcher
import com.wesleydonk.update.Parser
import com.wesleydonk.update.UpdateConfig
import com.wesleydonk.update.VersionApiModel
import com.wesleydonk.update.internal.managers.SystemDownloadManager

interface Controller {
    suspend fun execute()
}

class DefaultController(
    private val fetcher: Fetcher,
    private val parser: Parser,
    private val dataStore: DataStore,
    private val systemDownloadManager: SystemDownloadManager,
) : Controller {

    override suspend fun execute() {
        deleteAll()
        val result = fetcher.getLatestVersion()
        if (result != null) {
            storeUpdate(result)
        }
    }

    private suspend fun deleteAll() {
        dataStore.get()?.downloadId?.let { downloadId ->
            systemDownloadManager.delete(downloadId)
        }
        dataStore.deleteAll()
    }

    private suspend fun storeUpdate(update: VersionApiModel) {
        val version = parser.parse(update)
        dataStore.insert(version)
    }

    companion object {
        fun ofConfig(config: UpdateConfig): DefaultController = with(config) {
            return DefaultController(fetcher, parser, dataStore, systemDownloadManager)
        }
    }
}
