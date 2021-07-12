package com.wesleydonk.update.ui.internal.managers

import android.content.Context

private const val DIRECTORY_NAME = "Try"

interface FileManager {
    fun createFile(version: String): String
}

class FileManagerImpl(
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

        val fileName = "update-$version.apk"
        return "$directory/$fileName"
    }

}