package com.wesleydonk.update.internal.datastore

import android.content.Context
import com.wesleydonk.update.DataStore
import com.wesleydonk.update.Version
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Suppress("UnusedPrivateMember")
class LocalDataStore(context: Context) : DataStore {

    override suspend fun insert(version: Version) = Unit

    override suspend fun get(): Version? = null

    override fun getAsFlow(): Flow<Version> = flowOf()

    override suspend fun deleteAll() = Unit
}
