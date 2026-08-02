package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,
    val pollId: String,
    val username: String,
    val text: String,
    val timestamp: Long
)
