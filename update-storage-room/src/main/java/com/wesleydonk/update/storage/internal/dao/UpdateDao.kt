package com.wesleydonk.update.storage.internal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wesleydonk.update.storage.internal.model.VersionModel
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UpdateDao {

    fun firstOrNullAsFlow(): Flow<VersionModel?>

    @Query("SELECT * FROM version_table LIMIT 1")
    suspend fun firstOrNull(): VersionModel?

    @Update
    suspend fun update(version: VersionModel)

    @Insert
    suspend fun insert(version: VersionModel)

    @Query("DELETE FROM version_table")
    suspend fun deleteAll()
}