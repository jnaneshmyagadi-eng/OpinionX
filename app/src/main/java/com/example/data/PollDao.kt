package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PollDao {
    @Query("SELECT * FROM polls ORDER BY timestamp DESC")
    fun getAllPolls(): Flow<List<PollEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoll(poll: PollEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolls(polls: List<PollEntity>)

    @Update
    suspend fun updatePoll(poll: PollEntity)
}
