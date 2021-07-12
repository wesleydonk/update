package com.wesleydonk.update.storage.internal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wesleydonk.update.storage.internal.dao.UpdateDao
import com.wesleydonk.update.storage.internal.model.VersionModel

@Database(entities = [VersionModel::class], version = 1)
internal abstract class UpdateDatabase: RoomDatabase() {
    abstract fun updateDao(): UpdateDao
}