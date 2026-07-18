package com.kuriamind.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockDao {

    @Query("SELECT * FROM blocks ORDER BY name ASC")
    fun observeAll(): Flow<List<BlockEntity>>

    @Query("SELECT * FROM blocks WHERE id = :id")
    suspend fun getById(id: String): BlockEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(block: BlockEntity)

    @Update
    suspend fun update(block: BlockEntity)

    @Query("DELETE FROM blocks WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE blocks SET isActive = NOT isActive WHERE id = :id")
    suspend fun toggleActive(id: String)
}
