package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE polls ADD COLUMN mood TEXT")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `messages` (`id` TEXT NOT NULL, `senderId` TEXT NOT NULL, `receiverId` TEXT NOT NULL, `senderUsername` TEXT NOT NULL, `receiverUsername` TEXT NOT NULL, `text` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `isRead` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        db.execSQL("CREATE TABLE IF NOT EXISTS `notifications` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `actorUsername` TEXT NOT NULL, `text` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `isRead` INTEGER NOT NULL, `referenceId` TEXT, PRIMARY KEY(`id`))")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE polls ADD COLUMN isLiked INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE polls ADD COLUMN isSaved INTEGER NOT NULL DEFAULT 0")
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` TEXT NOT NULL, `username` TEXT NOT NULL, `displayName` TEXT NOT NULL, `bio` TEXT NOT NULL, `profileImageUri` TEXT, `currentMood` TEXT NOT NULL, `interests` TEXT NOT NULL, PRIMARY KEY(`id`))")
        db.execSQL("CREATE TABLE IF NOT EXISTS `comments` (`id` TEXT NOT NULL, `pollId` TEXT NOT NULL, `username` TEXT NOT NULL, `text` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        db.execSQL("CREATE TABLE IF NOT EXISTS `follows` (`followedId` TEXT NOT NULL, PRIMARY KEY(`followedId`))")
    }
}

@Database(entities = [PollEntity::class, UserEntity::class, CommentEntity::class, FollowEntity::class, MessageEntity::class, NotificationEntity::class], version = 4, exportSchema = false)
abstract class OpinionDatabase : RoomDatabase() {
    abstract fun pollDao(): PollDao
    abstract fun userDao(): UserDao
    abstract fun commentDao(): CommentDao
    abstract fun followDao(): FollowDao
    abstract fun messageDao(): MessageDao
    abstract fun notificationDao(): NotificationDao
}
