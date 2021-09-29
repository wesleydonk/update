package com.wesleydonk.update.fetcher

import android.content.Context
import co.infinum.princeofversions.*
import com.wesleydonk.update.CheckVersionResult
import com.wesleydonk.update.Fetcher
import com.wesleydonk.update.fetcher.internal.SemanticVersionParser
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PrinceOfVersionFetcher(
    context: Context,
    private val url: String,
) : Fetcher {

    private val loader = NetworkLoader(url)

    private val updateChecker = PrinceOfVersions.Builder()
        .withVersionParser(SemanticVersionParser())
        .build(context)

    private fun checkForUpdate(updaterCallback: UpdaterCallback): PrinceOfVersionsCancelable {
        return updateChecker.checkForUpdates(loader, updaterCallback)
    }

    override suspend fun latestVersionResult(): CheckVersionResult =
        suspendCancellableCoroutine { continuation ->
            if (url.isEmpty()) {
                continuation.resume(CheckVersionResult.NoUpdate)
                return@suspendCancellableCoroutine
            }

            val updateCheck = checkForUpdate(object : UpdaterCallback {
                override fun onNewUpdate(
                    version: String,
                    isMandatory: Boolean,
                    metadata: MutableMap<String, String>
                ) {
                    val updateId = version
                    val installUrl = metadata["install_url"].orEmpty()
                    continuation.resume(
                        CheckVersionResult.NewUpdate(
                            updateId,
                            mapOf("download_url" to installUrl)
                        )
                    )
                }

                override fun onNoUpdate(metadata: MutableMap<String, String>) {
                    continuation.resume(CheckVersionResult.NoUpdate)
                }

                override fun onError(error: Throwable) {
                    continuation.resume(CheckVersionResult.NoUpdate)
                }
            })

            continuation.invokeOnCancellation {
                updateCheck.cancel()
            }
        }
}