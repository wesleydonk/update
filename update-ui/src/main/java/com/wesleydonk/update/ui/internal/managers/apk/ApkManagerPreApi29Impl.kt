package com.wesleydonk.update.ui.internal.managers.apk

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

internal class ApkManagerPreApi29Impl(
    private val context: Context
) : ApkManager {

    override suspend fun install(apkFilePath: String, mimeType: String) {
        val authority = "${context.packageName}.update.fileprovider"
        val fileUri = FileProvider.getUriForFile(
            context,
            authority,
            File(apkFilePath)
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            flags += Intent.FLAG_GRANT_READ_URI_PERMISSION
            flags += Intent.FLAG_ACTIVITY_CLEAR_TOP
            data = fileUri
        }
        context.startActivity(intent)
    }
}
