package com.wesleydonk.update

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface Storage {
    suspend fun insert(version: Version)
    suspend fun update(version: Version)
    suspend fun get(): Version?
    fun getAsFlow(): Flow<Version>
    suspend fun deleteAll()
}