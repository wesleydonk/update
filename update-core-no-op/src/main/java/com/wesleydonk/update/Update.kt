package com.wesleydonk.update

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Suppress("unused", "UNUSED_PARAMETER")
class Update {

    fun synchronize() = Unit

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
