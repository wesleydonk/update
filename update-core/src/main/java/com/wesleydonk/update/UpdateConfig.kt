package com.wesleydonk.update

import android.content.Context
import com.wesleydonk.update.internal.datastore.LocalDataStore
import com.wesleydonk.update.internal.managers.SystemDownloadManager
import com.wesleydonk.update.internal.managers.SystemDownloadManagerImpl

class UpdateConfig(
    val dataStore: DataStore,
    val fetcher: Fetcher,
    val systemDownloadManager: SystemDownloadManager,
) {
    class Builder(
        private var fetcher: Fetcher? = null,
        private var dataStore: DataStore? = null,
        private var systemDownloadManager: SystemDownloadManager? = null,
    ) {

        fun fetcher(fetcher: Fetcher): Builder = apply {
            this@Builder.fetcher = fetcher
        }

        fun build(context: Context): UpdateConfig {
            dataStore = LocalDataStore(context)
            systemDownloadManager = SystemDownloadManagerImpl(context)

            return UpdateConfig(
                requireNotNull(dataStore),
                requireNotNull(fetcher),
                requireNotNull(systemDownloadManager)
            )
        }
    }
}
