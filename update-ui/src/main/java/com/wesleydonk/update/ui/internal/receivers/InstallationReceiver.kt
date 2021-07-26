package com.wesleydonk.update.ui.internal.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log

internal class InstallationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -1)
        Log.v("UPDATE", "UPDATE STATUS $status")
        when (status) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> startManualUserAction(context, intent)
            PackageInstaller.STATUS_SUCCESS -> executeInstalledTone()
            else -> {
                val msg = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)
                Log.e("UPDATE", "Status message: $msg")
            }
        }
    }

    // A signal is sent when the install is completed
    private fun executeInstalledTone() {
        ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100).apply {
            startTone(ToneGenerator.TONE_PROP_ACK)
        }
    }

    // whenever the install requires a manual action, an intent is received
    private fun startManualUserAction(context: Context, intent: Intent) {
        val installIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } ?: return
        context.startActivity(installIntent)
    }
}