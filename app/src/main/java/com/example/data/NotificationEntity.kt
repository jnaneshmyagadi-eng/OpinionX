package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val type: String,
    val actorUsername: String,
    val text: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val referenceId: String? = null
)
