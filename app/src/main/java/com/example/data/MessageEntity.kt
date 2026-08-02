package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val senderId: String,
    val receiverId: String,
    val senderUsername: String,
    val receiverUsername: String,
    val text: String,
    val timestamp: Long,
    val isRead: Boolean = false
)
