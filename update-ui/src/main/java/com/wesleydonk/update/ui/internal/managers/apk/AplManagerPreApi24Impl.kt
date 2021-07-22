package com.wesleydonk.update.ui.internal.managers.apk

import android.content.Context
import android.content.Intent
import android.net.Uri

internal class AplManagerPreApi24Impl(
    private val context: Context
) : ApkManager {
    override suspend fun install(apkFilePath: String, mimeType: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            flags += Intent.FLAG_ACTIVITY_CLEAR_TOP
            setDataAndType(Uri.parse("file://$apkFilePath"), mimeType)
        }
        context.startActivity(intent)
    }
}
