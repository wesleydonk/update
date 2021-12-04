package com.wesleydonk.update.internal.storage

import android.content.Context
import com.wesleydonk.update.Storage
import com.wesleydonk.update.Version
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Suppress("UnusedPrivateMember")
class RoomStorage(context: Context) : Storage {

    override suspend fun insert(version: Version) = Unit

    override suspend fun get(): Version? = null

    override fun getAsFlow(): Flow<Version> = flowOf()

    override suspend fun deleteAll() = Unit
}
