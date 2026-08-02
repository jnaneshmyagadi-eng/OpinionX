package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: String = "me",
    val username: String = "me_user",
    val displayName: String = "My Name",
    val bio: String = "This is my bio.",
    val profileImageUri: String? = null,
    val currentMood: String = "Happy",
    val interests: String = "Gaming"
)
