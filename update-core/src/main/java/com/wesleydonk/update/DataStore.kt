package com.wesleydonk.update

import kotlinx.coroutines.flow.Flow

@Deprecated("Implement new DataStore based on file system")
interface DataStore {
    suspend fun insert(version: Version)
    suspend fun get(): Version?
    fun getAsFlow(): Flow<Version?>
    suspend fun deleteAll()
}
