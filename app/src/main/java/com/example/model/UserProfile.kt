package com.example.model

data class UserProfile(
    val id: String,
    val name: String,
    val avatarUrl: Int,
    val age: Int,
    val interest: String,
    val distanceMiles: Int,
    val currentMood: String,
    val isFollowed: Boolean = false
)
