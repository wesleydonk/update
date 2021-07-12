package com.wesleydonk.update.storage.internal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "version_table")
internal data class VersionModel(
    @PrimaryKey val version: String,
    @ColumnInfo(name = "download_url") val downloadUrl: String,
    @ColumnInfo(name = "download_id") val downloadId: Long?,
)