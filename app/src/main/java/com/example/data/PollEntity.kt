package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Poll

@Entity(tableName = "polls")
data class PollEntity(
    @PrimaryKey val id: String,
    val username: String,
    val avatarUrl: Int,
    val timeAgo: String,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionAImageUri: String?,
    val optionBImageUri: String?,
    val votesA: Int,
    val votesB: Int,
    val hasVoted: Boolean,
    val selectedOption: String?,
    val likes: Int,
    val comments: Int,
    val timestamp: Long,
    val mood: String?,
    val isLiked: Boolean,
    val isSaved: Boolean
)

fun PollEntity.toPoll(): Poll {
    return Poll(
        id = id,
        username = username,
        avatarUrl = avatarUrl,
        timeAgo = timeAgo,
        question = question,
        optionA = optionA,
        optionB = optionB,
        optionAImageUri = optionAImageUri,
        optionBImageUri = optionBImageUri,
        votesA = votesA,
        votesB = votesB,
        hasVoted = hasVoted,
        selectedOption = selectedOption,
        likes = likes,
        comments = comments,
        mood = mood,
        isLiked = isLiked,
        isSaved = isSaved
    )
}

fun Poll.toEntity(): PollEntity {
    return PollEntity(
        id = id,
        username = username,
        avatarUrl = avatarUrl,
        timeAgo = timeAgo,
        question = question,
        optionA = optionA,
        optionB = optionB,
        optionAImageUri = optionAImageUri,
        optionBImageUri = optionBImageUri,
        votesA = votesA,
        votesB = votesB,
        hasVoted = hasVoted,
        selectedOption = selectedOption,
        likes = likes,
        comments = comments,
        timestamp = System.currentTimeMillis(),
        mood = mood,
        isLiked = isLiked,
        isSaved = isSaved
    )
}
