package com.wesleydonk.update.fetcher

import android.content.Context
import co.infinum.princeofversions.*
import com.wesleydonk.update.CheckVersionResult
import com.wesleydonk.update.Fetcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PrinceOfVersionFetcher(
    context: Context,
    private val url: String,
) : Fetcher {

    private val loader = NetworkLoader(url)

    private val updateChecker = PrinceOfVersions.Builder().build(context)

    private fun checkForUpdate(updaterCallback: UpdaterCallback): PrinceOfVersionsCancelable {
        return updateChecker.checkForUpdates(loader, updaterCallback)
    }

    override suspend fun latestVersionResult(): CheckVersionResult? =
        suspendCancellableCoroutine { continuation ->
            if (url.isEmpty()) {
                continuation.resume(null)
                return@suspendCancellableCoroutine
            }

            val callback = object : UpdaterCallback {
                override fun onSuccess(result: UpdateResult) {
                    val versionResult = when (result.status) {
                        UpdateStatus.REQUIRED_UPDATE_NEEDED,
                        UpdateStatus.NEW_UPDATE_AVAILABLE -> CheckVersionResult(
                            result.updateVersion.toString(),
                            result.metadata
                        )
                        else -> null
                    }
                    continuation.resume(versionResult)
                }

                override fun onError(error: Throwable) {
                    continuation.resume(null)
                }
            }
            val updateCheck = checkForUpdate(callback)

            continuation.invokeOnCancellation {
                updateCheck.cancel()
            }
        }
}