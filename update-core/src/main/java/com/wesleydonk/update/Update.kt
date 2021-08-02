package com.wesleydonk.update

import com.wesleydonk.update.internal.controller.DefaultController
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Update(
    private val config: UpdateConfig
) {

    private val controller = DefaultController.ofConfig(config)

    private val scope = MainScope()

    fun synchronize() {
        scope.launch {
            controller.execute()
        }
    }

    fun getLatestVersion(): Flow<Version> {
        return config.storage.getAsFlow()
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
}