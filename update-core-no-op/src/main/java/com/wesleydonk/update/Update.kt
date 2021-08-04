package com.wesleydonk.update

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Update {

    fun synchronize() {
    }

    fun getLatestVersion(): Flow<Version> {
        return flowOf<Version>()
    }

    class Builder {

        fun config(config: UpdateConfig) = apply {
            return this
        }

        fun build(): Update {
            return Update()
        }
    }
}