package com.wesleydonk.update

import android.content.Context
import com.wesleydonk.update.internal.managers.SystemDownloadManager
import com.wesleydonk.update.internal.managers.SystemDownloadManagerImpl
import com.wesleydonk.update.internal.storage.RoomStorage

class UpdateConfig(
    val parser: Parser,
    val storage: Storage,
    val fetcher: Fetcher,
    val systemDownloadManager: SystemDownloadManager,
) {
    class Builder(
        private var parser: Parser? = null,
        private var fetcher: Fetcher? = null,
        private var storage: Storage? = null,
        private var systemDownloadManager: SystemDownloadManager? = null,
    ) {

        fun parser(parser: Parser): Builder = apply {
            this@Builder.parser = parser
        }

        fun fetcher(fetcher: Fetcher): Builder = apply {
            this@Builder.fetcher = fetcher
        }

        fun storage(storage: Storage): Builder = this

        fun build(context: Context): UpdateConfig {
            storage = RoomStorage(context)
            systemDownloadManager = SystemDownloadManagerImpl(context)

            return UpdateConfig(
                requireNotNull(parser),
                requireNotNull(storage),
                requireNotNull(fetcher),
                requireNotNull(systemDownloadManager)
            )
        }
    }
}