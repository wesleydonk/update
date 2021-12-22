package com.wesleydonk.update

import android.content.Context

class UpdateConfig(
    val context: Context,
    val parser: Parser,
    val dataStore: DataStore,
    val fetcher: Fetcher
) {
    class Builder(
        private var parser: Parser? = null,
        private var fetcher: Fetcher? = null,
        private var dataStore: DataStore? = null,
    ) {

        fun parser(parser: Parser): Builder = apply {
            this@Builder.parser = parser
        }

        fun fetcher(fetcher: Fetcher): Builder = apply {
            this@Builder.fetcher = fetcher
        }

        fun build(context: Context): UpdateConfig {
            return UpdateConfig(
                context,
                requireNotNull(parser),
                requireNotNull(dataStore),
                requireNotNull(fetcher),
            )
        }
    }
}
