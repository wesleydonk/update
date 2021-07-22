package com.wesleydonk.update.ui.internal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

internal object IntentUtil {

    fun apkInstall(context: Context, filePath: String, mimeType: String): Intent = throw NullPointerException()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            val authority = "${context.packageName}.update.fileprovider"
//            val fileUri = FileProvider.getUriForFile(
//                context,
//                authority,
//                File(filePath)
//            )
//            Intent(Intent.ACTION_VIEW).apply {
//                flags += Intent.FLAG_GRANT_READ_URI_PERMISSION
//                flags += Intent.FLAG_ACTIVITY_CLEAR_TOP
//                data = fileUri
//            }
//        } else {
//            Intent(Intent.ACTION_VIEW).apply {
//                flags += Intent.FLAG_ACTIVITY_CLEAR_TOP
//                setDataAndType(Uri.parse("file://$filePath"), mimeType)
//            }
//        }

//        val intent =  Intent(Intent.ACTION_INSTALL_PACKAGE);
//    intent.setDataAndType(Uri.fromFile(new File(location + "myAPK.apk")),
//    "application/vnd.android.package-archive");
//    intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
//    startActivityForResult(intent, 0);
}