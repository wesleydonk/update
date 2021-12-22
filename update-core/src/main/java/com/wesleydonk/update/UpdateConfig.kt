package com.wesleydonk.update

import android.content.Context
import com.wesleydonk.update.internal.managers.SystemDownloadManager
import com.wesleydonk.update.internal.managers.SystemDownloadManagerImpl
import com.wesleydonk.update.internal.datastore.LocalDataStore

class UpdateConfig(
    val parser: Parser,
    val dataStore: DataStore,
    val fetcher: Fetcher,
    val systemDownloadManager: SystemDownloadManager,
) {
    class Builder(
        private var parser: Parser? = null,
        private var fetcher: Fetcher? = null,
        private var dataStore: DataStore? = null,
        private var systemDownloadManager: SystemDownloadManager? = null,
    ) {

        fun parser(parser: Parser): Builder = apply {
            this@Builder.parser = parser
        }

        fun fetcher(fetcher: Fetcher): Builder = apply {
            this@Builder.fetcher = fetcher
        }

        fun build(context: Context): UpdateConfig {
            dataStore = LocalDataStore(context)
            systemDownloadManager = SystemDownloadManagerImpl(context)

            return UpdateConfig(
                requireNotNull(parser),
                requireNotNull(dataStore),
                requireNotNull(fetcher),
                requireNotNull(systemDownloadManager)
            )
        }
    }
}
