package com.wesleydonk.update.storage

import android.content.Context
import androidx.room.Room
import com.wesleydonk.update.Storage
import com.wesleydonk.update.Version
import com.wesleydonk.update.storage.internal.database.UpdateDatabase
import com.wesleydonk.update.storage.internal.extensions.fromModel
import com.wesleydonk.update.storage.internal.extensions.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATABASE_NAME = "DATABASE_NAME"

class RoomStorage(context: Context) : Storage {

    private val database = Room
        .databaseBuilder(context, UpdateDatabase::class.java, DATABASE_NAME)
        .build()

    private val updateDao = database.updateDao()

    override suspend fun insert(version: Version) {
        updateDao.insert(version.toModel())
    }

    override suspend fun update(version: Version) {
        return updateDao.update(version.toModel())
    }

    override suspend fun get(): Version? {
        return updateDao.firstOrNull()?.fromModel()
    }

    override fun getAsFlow(): Flow<Version?> {
        return updateDao.firstOrNullAsFlow().map { model ->
            model?.fromModel()
        }
    }

    override suspend fun deleteAll() {
        return updateDao.deleteAll()
    }
}