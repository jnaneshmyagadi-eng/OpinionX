package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.R
import com.example.data.OpinionDatabase
import com.example.data.PollRepository
import com.example.model.Poll
import com.example.model.UserProfile
import com.example.data.UserEntity
import com.example.data.FollowEntity
import com.example.data.CommentEntity
import com.example.data.MessageEntity
import com.example.data.NotificationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class OpinionViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Room.databaseBuilder(
        application,
        OpinionDatabase::class.java,
        "opinionx_db"
    ).addMigrations(com.example.data.MIGRATION_1_2, com.example.data.MIGRATION_2_3, com.example.data.MIGRATION_3_4)
     .build()

    private val repository = PollRepository(
        database.pollDao(),
        database.userDao(),
        database.commentDao(),
        database.followDao(),
        database.messageDao(),
        database.notificationDao()
    )

    val polls: StateFlow<List<Poll>> = repository.allPolls
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val follows: StateFlow<List<FollowEntity>> = repository.allFollows
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userProfile: StateFlow<UserEntity?> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _mockUsers = MutableStateFlow<List<UserProfile>>(emptyList())

    val mockUsers: StateFlow<List<UserProfile>> = _mockUsers

    init {
        // Prepopulate with sample data if empty
        viewModelScope.launch {
            _mockUsers.value = listOf(
                UserProfile(id = "u1", name = "Alex", avatarUrl = R.drawable.img_avatar_1_1785475392285, age = 22, interest = "Gaming", distanceMiles = 2, currentMood = "Happy"),
                UserProfile(id = "u2", name = "Sam", avatarUrl = R.drawable.img_avatar_2_1785475405161, age = 24, interest = "Music", distanceMiles = 5, currentMood = "Bored"),
                UserProfile(id = "u3", name = "Maya", avatarUrl = R.drawable.img_avatar_3_1785475415726, age = 21, interest = "Art", distanceMiles = 3, currentMood = "Chill")
            )
            val currentPolls = repository.allPolls.first()
            if (currentPolls.isEmpty()) {
                    val samplePolls = listOf(
                        Poll(
                            id = UUID.randomUUID().toString(),
                            username = "alex_dev",
                            avatarUrl = R.drawable.img_avatar_1_1785475392285,
                            timeAgo = "2h ago",
                            question = "Which tech stack for my new startup?",
                            optionA = "Kotlin + Compose",
                            optionB = "React Native",
                            votesA = 120,
                            votesB = 85,
                            likes = 45,
                            comments = 12,
                            mood = "Thinking"
                        ),
                        Poll(
                            id = UUID.randomUUID().toString(),
                            username = "sarah_design",
                            avatarUrl = R.drawable.img_avatar_2_1785475405161,
                            timeAgo = "5h ago",
                            question = "What should be the main brand color?",
                            optionA = "Vibrant Purple",
                            optionB = "Neon Orange",
                            votesA = 400,
                            votesB = 412,
                            likes = 122,
                            comments = 34,
                            mood = "Excited"
                        ),
                        Poll(
                            id = UUID.randomUUID().toString(),
                            username = "crypto_king",
                            avatarUrl = R.drawable.img_avatar_3_1785475415726,
                            timeAgo = "1d ago",
                            question = "Will Bitcoin hit 100k this year?",
                            optionA = "Absolutely \uD83D\uDE80",
                            optionB = "No way \uD83D\uDC3B",
                            votesA = 2030,
                            votesB = 890,
                            likes = 540,
                            comments = 190,
                            mood = "Bored"
                        )
                    )
                    repository.insertPolls(samplePolls)
                }
        }
    }

    fun vote(poll: Poll, option: String) {
        if (poll.hasVoted) return
        val newA = if (option == "A") poll.votesA + 1 else poll.votesA
        val newB = if (option == "B") poll.votesB + 1 else poll.votesB
        
        val updatedPoll = poll.copy(
            hasVoted = true,
            selectedOption = option,
            votesA = newA,
            votesB = newB
        )
        viewModelScope.launch {
            repository.updatePoll(updatedPoll)
        }
    }

    fun createPoll(question: String, optionA: String, optionB: String, imageUriA: String?, imageUriB: String?, mood: String?) {
        val newPoll = Poll(
            id = UUID.randomUUID().toString(),
            username = "me_user",
            avatarUrl = R.drawable.img_avatar_1_1785475392285, // local dummy avatar
            timeAgo = "Just now",
            question = question,
            optionA = optionA,
            optionB = optionB,
            optionAImageUri = imageUriA,
            optionBImageUri = imageUriB,
            votesA = 0,
            votesB = 0,
            hasVoted = false,
            selectedOption = null,
            likes = 0,
            comments = 0,
            mood = mood
        )
        viewModelScope.launch {
            repository.insertPoll(newPoll)
        }
    }

    fun calculateVibeScore(userMood: String, targetMood: String, userInterest: String, targetInterest: String, distance: Int): Int {
        var score = 0
        if (userMood == targetMood) score += 60
        else if (isSimilarMood(userMood, targetMood)) score += 10
        if (userInterest == targetInterest) score += 25
        if (distance <= 5) score += 5
        return minOf(score, 100)
    }

    private fun isSimilarMood(m1: String, m2: String): Boolean {
        val highEnergy = listOf("Happy", "Excited", "Motivated")
        val lowEnergy = listOf("Bored", "Sad", "Chill", "Thinking")
        return (highEnergy.contains(m1) && highEnergy.contains(m2)) || (lowEnergy.contains(m1) && lowEnergy.contains(m2))
    }

    fun matchVibe(userId: String) {
        _mockUsers.value = _mockUsers.value.map {
            if (it.id == userId) it.copy(isFollowed = true) else it
        }
        viewModelScope.launch {
            val myUsername = userProfile.value?.username ?: "me_user"
            repository.insertNotification(
                NotificationEntity(
                    id = UUID.randomUUID().toString(),
                    type = "VibeMatch",
                    actorUsername = myUsername,
                    text = "You have a new Vibe Match with $userId!",
                    timestamp = System.currentTimeMillis(),
                    referenceId = userId
                )
            )
        }
    }

    fun toggleLike(poll: Poll) {
        val updatedPoll = poll.copy(
            isLiked = !poll.isLiked,
            likes = if (poll.isLiked) poll.likes - 1 else poll.likes + 1
        )
        viewModelScope.launch {
            repository.updatePoll(updatedPoll)
            if (!poll.isLiked) {
                val myUsername = userProfile.value?.username ?: "me_user"
                repository.insertNotification(
                    NotificationEntity(
                        id = UUID.randomUUID().toString(),
                        type = "Like",
                        actorUsername = myUsername,
                        text = "$myUsername liked your poll",
                        timestamp = System.currentTimeMillis(),
                        referenceId = poll.id
                    )
                )
            }
        }
    }

    fun toggleSave(poll: Poll) {
        val updatedPoll = poll.copy(
            isSaved = !poll.isSaved
        )
        viewModelScope.launch { repository.updatePoll(updatedPoll) }
    }

    fun toggleFollow(userId: String) {
        viewModelScope.launch {
            val currentFollows = follows.value
            val isFollowing = currentFollows.any { it.followedId == userId }
            if (isFollowing) {
                repository.deleteFollow(userId)
            } else {
                repository.insertFollow(FollowEntity(followedId = userId))
                val myUsername = userProfile.value?.username ?: "me_user"
                repository.insertNotification(
                    NotificationEntity(
                        id = UUID.randomUUID().toString(),
                        type = "Follow",
                        actorUsername = myUsername,
                        text = "$myUsername started following you",
                        timestamp = System.currentTimeMillis(),
                        referenceId = userId
                    )
                )
            }
        }
    }

    fun getCommentsForPoll(pollId: String) = repository.getCommentsForPoll(pollId)

    fun addComment(pollId: String, comment: CommentEntity) {
        viewModelScope.launch {
            repository.insertComment(comment)
            val poll = repository.allPolls.first().find { it.id == pollId }
            if (poll != null) {
                repository.updatePoll(poll.copy(comments = poll.comments + 1))
                val myUsername = userProfile.value?.username ?: "me_user"
                repository.insertNotification(
                    NotificationEntity(
                        id = UUID.randomUUID().toString(),
                        type = "Comment",
                        actorUsername = myUsername,
                        text = "$myUsername commented on your poll",
                        timestamp = System.currentTimeMillis(),
                        referenceId = poll.id
                    )
                )
            }
        }
    }
    fun updateUserProfile(user: UserEntity) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val unreadNotificationCount: StateFlow<Int> = repository.unreadNotificationCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun getMessagesBetweenUsers(userId1: String, userId2: String) = repository.getMessagesBetweenUsers(userId1, userId2)
    fun getAllMessagesForUser(userId: String) = repository.getAllMessagesForUser(userId)

    fun sendMessage(senderId: String, receiverId: String, senderUsername: String, receiverUsername: String, text: String) {
        viewModelScope.launch {
            val message = MessageEntity(
                id = UUID.randomUUID().toString(),
                senderId = senderId,
                receiverId = receiverId,
                senderUsername = senderUsername,
                receiverUsername = receiverUsername,
                text = text,
                timestamp = System.currentTimeMillis()
            )
            repository.insertMessage(message)
            
            // For MVP: simply insert a notification so we can see it in the UI
            val notif = NotificationEntity(
                id = UUID.randomUUID().toString(),
                type = "Message",
                actorUsername = senderUsername,
                text = "$senderUsername sent you a message",
                timestamp = System.currentTimeMillis(),
                referenceId = senderId 
            )
            repository.insertNotification(notif)
        }
    }

    fun markMessagesAsRead(userId: String, otherUserId: String) {
        viewModelScope.launch {
            repository.markMessagesAsRead(userId, otherUserId)
        }
    }

    fun markNotificationAsRead(notificationId: String) {
        viewModelScope.launch {
            repository.markNotificationAsRead(notificationId)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }
}
