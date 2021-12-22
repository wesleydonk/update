package com.wesleydonk.update

import android.content.Context

class UpdateConfig(
    val context: Context,
    val dataStore: DataStore,
    val fetcher: Fetcher
) {
    class Builder(
        private var fetcher: Fetcher? = null,
        private var dataStore: DataStore? = null,
    ) {

        fun fetcher(fetcher: Fetcher): Builder = apply {
            this@Builder.fetcher = fetcher
        }

        fun build(context: Context): UpdateConfig {
            return UpdateConfig(
                context,
                requireNotNull(dataStore),
                requireNotNull(fetcher),
            )
        }
    }
}
