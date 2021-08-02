package com.wesleydonk.update.internal.dao

import androidx.room.*
import com.wesleydonk.update.internal.database.model.VersionModel
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UpdateDao {

    @Query("SELECT * FROM version_table LIMIT 1")
    fun firstOrNullAsFlow(): Flow<VersionModel?>

    @Query("SELECT * FROM version_table")
    suspend fun getVersionList(): List<VersionModel>

    @Update
    suspend fun update(version: VersionModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(version: VersionModel)

    @Query("DELETE FROM version_table")
    suspend fun deleteAll()
}