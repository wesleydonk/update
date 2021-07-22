package com.wesleydonk.update.ui.internal.managers.apk

internal interface ApkManager {
    suspend fun install(apkFilePath: String, mimeType: String)
}

internal const val PI_INSTALL = 4
