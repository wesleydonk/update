package com.wesleydonk.update

import kotlinx.coroutines.flow.Flow

interface DataStore {
    suspend fun insert(version: Version)
    suspend fun get(): Version?
    fun getAsFlow(): Flow<Version?>
    suspend fun deleteAll()
}
