package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE pollId = :pollId ORDER BY timestamp ASC")
    fun getCommentsForPoll(pollId: String): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)
}
