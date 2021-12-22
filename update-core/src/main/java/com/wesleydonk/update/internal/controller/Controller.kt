package com.wesleydonk.update.internal.controller

import com.wesleydonk.update.DataStore
import com.wesleydonk.update.DefaultParser
import com.wesleydonk.update.Fetcher
import com.wesleydonk.update.Parser
import com.wesleydonk.update.UpdateConfig
import com.wesleydonk.update.VersionApiModel
import com.wesleydonk.update.internal.managers.SystemDownloadManager

interface Controller {
    suspend fun execute()
}

internal class DefaultController(
    private val fetcher: Fetcher,
    private val dataStore: DataStore,
    private val systemDownloadManager: SystemDownloadManager,
    private val parser: Parser = DefaultParser(),
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

    private suspend fun storeUpdate(model: VersionApiModel) {
        val version = parser.parse(model)
        dataStore.insert(version)
    }

    companion object {
        fun ofConfig(config: UpdateConfig): DefaultController = with(config) {
            return DefaultController(fetcher, dataStore, systemDownloadManager)
        }
    }
}
