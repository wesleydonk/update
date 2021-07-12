package com.wesleydonk.update

import android.content.Context
import com.wesleydonk.update.internal.controller.DefaultController
import com.wesleydonk.update.internal.managers.SystemDownloadManagerImpl

class Update(
    private val config: UpdateConfig
) {

    init {
        instance = this
    }

    private val controller = DefaultController.ofConfig(config)

    suspend fun checkVersion(): Version? {
        controller.execute()
        return config.storage.get()
    }

    suspend fun getStoredVersion(): Version? {
        return config.storage.get()
    }

    class Builder(
        private var config: UpdateConfig? = null
    ) {

        fun config(config: UpdateConfig) = apply {
            this.config = config
            return this
        }

        fun build(): Update {
            return Update(requireNotNull(config))
        }
    }

    companion object {
        @Volatile
        private var instance: Update? = null

        fun getInstance(): Update {
            return instance
                ?: throw IllegalStateException("Update was never created before calling the instance property")
        }
    }
}