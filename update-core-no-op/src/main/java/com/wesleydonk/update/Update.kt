package com.wesleydonk.update

class Update(
    private val config: UpdateConfig
) {

    suspend fun checkVersion(): Version? {
        return null
    }

    suspend fun getStoredVersion(): Version? {
        return null
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