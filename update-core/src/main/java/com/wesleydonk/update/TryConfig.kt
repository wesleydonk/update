package com.wesleydonk.update

import android.content.Context

class TryConfig(
    val context: Context,
    val strategy: Strategy,
    val parser: Parser,
    val storage: Storage,
    val fetcher: Fetcher
) {
    class Builder(
        private var strategy: Strategy? = null,
        private var parser: Parser? = null,
        private var fetcher: Fetcher? = null,
        private var storage: Storage? = null,
    ) {

        fun strategy(strategy: Strategy): Builder = apply {
            this@Builder.strategy = strategy
        }

        fun parser(parser: Parser): Builder = apply {
            this@Builder.parser = parser
        }

        fun fetcher(fetcher: Fetcher): Builder = apply {
            this@Builder.strategy = strategy
        }

        fun storage(storage: Storage): Builder = apply {
            this@Builder.storage = storage
        }

        fun build(context: Context): TryConfig {
            return TryConfig(
                context,
                requireNotNull(strategy),
                requireNotNull(parser),
                requireNotNull(storage),
                requireNotNull(fetcher),
            )
        }
    }
}