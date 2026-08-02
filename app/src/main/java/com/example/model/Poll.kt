package com.example.model

data class Poll(
    val id: String,
    val username: String,
    val avatarUrl: Int, // Using drawable res ID for local MVP
    val timeAgo: String,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionAImageUri: String? = null,
    val optionBImageUri: String? = null,
    val votesA: Int,
    val votesB: Int,
    val hasVoted: Boolean = false,
    val selectedOption: String? = null,
    val likes: Int,
    val comments: Int,
    val mood: String? = null,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false
) {
    val totalVotes get() = votesA + votesB
    fun percentA(): Float = if (totalVotes == 0) 0f else votesA.toFloat() / totalVotes
    fun percentB(): Float = if (totalVotes == 0) 0f else votesB.toFloat() / totalVotes
}
