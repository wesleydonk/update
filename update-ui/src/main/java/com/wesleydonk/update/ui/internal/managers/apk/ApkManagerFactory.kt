package com.wesleydonk.update.ui.internal.managers.apk

import android.content.Context
import android.os.Build

internal class ApkManagerFactory(
    private val context: Context
) : ApkManager {
    override suspend fun install(apkFilePath: String, mimeType: String) {
        val manager = when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.N -> AplManagerPreApi24Impl(context)
            Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> ApkManagerPreApi29Impl(context)
            else -> ApkManagerImpl(context)
        }
        manager.install(apkFilePath, mimeType)
    }
}