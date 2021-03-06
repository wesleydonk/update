package com.wesleydonk.update.fetcher

import android.content.Context
import co.infinum.princeofversions.NetworkLoader
import co.infinum.princeofversions.PrinceOfVersions
import co.infinum.princeofversions.UpdaterCallback
import com.wesleydonk.update.Fetcher
import com.wesleydonk.update.VersionApiModel
import com.wesleydonk.update.fetcher.internal.SemanticVersionParser
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

class PrinceOfVersionFetcher(
    context: Context,
    private val url: String,
) : Fetcher {

    private val loader = NetworkLoader(url)

    private val princeOfVersions = PrinceOfVersions.Builder()
        .withVersionParser(SemanticVersionParser())
        .build(context)

    override suspend fun getLatestVersion(): VersionApiModel? {
        if (url.isEmpty()) {
            return null
        }
        return suspendCancellableCoroutine { continuation ->
            val callback = object : UpdaterCallback {
                override fun onNewUpdate(
                    version: String,
                    isMandatory: Boolean,
                    metadata: MutableMap<String, String>
                ) {
                    val installUrl = metadata["install_url"].orEmpty()
                    continuation.resume(
                        VersionApiModel(version, installUrl)
                    )
                }

                override fun onNoUpdate(metadata: MutableMap<String, String>) {
                    continuation.resume(null)
                }

                override fun onError(error: Throwable) {
                    continuation.resume(null)
                }
            }

            val updateCheck = princeOfVersions.checkForUpdates(loader, callback)

            continuation.invokeOnCancellation {
                updateCheck.cancel()
            }
        }
    }
}
