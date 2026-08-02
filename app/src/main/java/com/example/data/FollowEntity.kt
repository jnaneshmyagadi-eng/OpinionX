package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "follows")
data class FollowEntity(
    @PrimaryKey val followedId: String
)
