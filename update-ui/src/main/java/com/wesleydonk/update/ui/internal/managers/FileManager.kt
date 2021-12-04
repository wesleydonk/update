package com.wesleydonk.update.ui.internal.managers

import android.content.Context

private const val DIRECTORY_NAME = "Update"
private const val FILE_NAME_FORMAT = "update-%s.apk"

internal interface FileManager {
    fun createFile(version: String): String
}

internal class FileManagerImpl(
    private val context: Context
) : FileManager {
    override fun createFile(version: String): String {
        val directory = context.getExternalFilesDir(DIRECTORY_NAME)
            ?: throw IllegalArgumentException("Directory cannot be null")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        directory.listFiles().orEmpty().forEach { file ->
            file.deleteRecursively()
        }

        val fileName = FILE_NAME_FORMAT.format(version)
        return "$directory/$fileName"
    }
}
