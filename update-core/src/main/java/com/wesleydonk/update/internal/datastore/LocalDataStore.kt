package com.wesleydonk.update.internal.datastore

import android.content.Context
import androidx.room.Room
import com.wesleydonk.update.DataStore
import com.wesleydonk.update.Version
import com.wesleydonk.update.internal.database.UpdateDatabase
import com.wesleydonk.update.internal.database.model.VersionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

private const val DATABASE_NAME = "DATABASE_NAME"

private fun Version.toModel(): VersionModel = VersionModel(id, downloadUrl, downloadId)

private fun VersionModel.fromModel(): Version = Version(version, downloadUrl, downloadId)

class LocalDataStore(context: Context) : DataStore {

    private val updateDao = getDatabase(context).updateDao()

    override suspend fun insert(version: Version) {
        updateDao.insert(version.toModel())
    }

    override suspend fun get(): Version? {
        return updateDao.getVersionList()
            .firstOrNull()
            ?.fromModel()
    }

    override fun getAsFlow(): Flow<Version> {
        return updateDao.firstOrNullAsFlow()
            .filterNotNull()
            .map { model -> model.fromModel() }
            .distinctUntilChanged { old, new -> old.id == new.id }
    }

    override suspend fun deleteAll() {
        return updateDao.deleteAll()
    }

    companion object {

        @Volatile
        private var database: UpdateDatabase? = null

        internal fun getDatabase(context: Context): UpdateDatabase {
            return database ?: synchronized(this) {
                Room
                    .databaseBuilder(context, UpdateDatabase::class.java, DATABASE_NAME)
                    .build().also { database ->
                        this@Companion.database = database
                    }
            }
        }
    }
}
