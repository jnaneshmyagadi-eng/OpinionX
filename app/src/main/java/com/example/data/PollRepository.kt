package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PollRepository(
    private val pollDao: PollDao,
    private val userDao: UserDao,
    private val commentDao: CommentDao,
    private val followDao: FollowDao,
    private val messageDao: MessageDao,
    private val notificationDao: NotificationDao
) {
    val allPolls: Flow<List<com.example.model.Poll>> = pollDao.getAllPolls().map { entities ->
        entities.map { it.toPoll() }
    }

    val userProfile: Flow<UserEntity?> = userDao.getUserProfile()
    val allFollows: Flow<List<FollowEntity>> = followDao.getAllFollows()

    fun getCommentsForPoll(pollId: String): Flow<List<CommentEntity>> = commentDao.getCommentsForPoll(pollId)

    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    suspend fun insertComment(comment: CommentEntity) = commentDao.insertComment(comment)

    suspend fun insertFollow(follow: FollowEntity) = followDao.insertFollow(follow)
    suspend fun deleteFollow(followedId: String) = followDao.deleteFollow(followedId)

    suspend fun insertPoll(poll: com.example.model.Poll) {
        pollDao.insertPoll(poll.toEntity())
    }

    suspend fun insertPolls(polls: List<com.example.model.Poll>) {
        pollDao.insertPolls(polls.map { it.toEntity() })
    }

    suspend fun updatePoll(poll: com.example.model.Poll) {
        pollDao.updatePoll(poll.toEntity())
    }

    fun getMessagesBetweenUsers(userId1: String, userId2: String) = messageDao.getMessagesBetweenUsers(userId1, userId2)
    fun getAllMessagesForUser(userId: String) = messageDao.getAllMessagesForUser(userId)
    suspend fun insertMessage(message: MessageEntity) = messageDao.insertMessage(message)
    suspend fun markMessagesAsRead(userId: String, otherUserId: String) = messageDao.markMessagesAsRead(userId, otherUserId)

    val allNotifications = notificationDao.getAllNotifications()
    val unreadNotificationCount = notificationDao.getUnreadCount()
    suspend fun insertNotification(notification: NotificationEntity) = notificationDao.insertNotification(notification)
    suspend fun markNotificationAsRead(notificationId: String) = notificationDao.markAsRead(notificationId)
    suspend fun markAllNotificationsAsRead() = notificationDao.markAllAsRead()
}
