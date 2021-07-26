package com.wesleydonk.update.ui.internal.managers.apk

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.wesleydonk.update.ui.internal.receivers.InstallationReceiver
import kotlinx.coroutines.Dispatchers
import java.io.File

internal class ApkManagerImpl(
    private val context: Context
) : ApkManager {

    private val installer = context.packageManager.packageInstaller
    private val resolver = context.contentResolver

    override suspend fun install(apkFilePath: String, mimeType: String) {
        with(Dispatchers.IO) {
            val apkUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.update.fileprovider",
                File(apkFilePath)
            )
            resolver.openInputStream(apkUri)?.use { apkStream ->
                val length =
                    DocumentFile.fromSingleUri(context, apkUri)?.length() ?: -1
                val params =
                    PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
                val sessionId = installer.createSession(params)
                val session = installer.openSession(sessionId)
                session.openWrite(NAME, 0, length).use { sessionStream ->
                    apkStream.copyTo(sessionStream)
                    session.fsync(sessionStream)
                }

                val intent = Intent(context, InstallationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    PI_INSTALL,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                session.commit(pendingIntent.intentSender)
                session.close()
            }
        }
    }

    companion object {
        private const val NAME = "SESSION_NAME"
    }
}