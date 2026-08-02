package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.model.UserProfile
import com.example.ui.components.OpinionXTopBar
import com.example.ui.theme.BrandPinkLight
import com.example.ui.theme.BrandPurple
import com.example.viewmodel.OpinionViewModel

@Composable
fun ChatListScreen(
    viewModel: OpinionViewModel = viewModel(),
    onNavigateToChat: (String) -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val users by viewModel.mockUsers.collectAsState()
    val allMessages by viewModel.getAllMessagesForUser("me").collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OpinionXTopBar(onNotificationsClick = onNavigateToNotifications)

        Text(
            text = "Messages",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(users, key = { it.id }) { user ->
                val userMessages = allMessages.filter { it.senderId == user.id || it.receiverId == user.id }
                val lastMessage = userMessages.maxByOrNull { it.timestamp }
                val unreadCount = userMessages.count { it.senderId == user.id && !it.isRead }

                ChatListItem(
                    user = user,
                    lastMessageText = lastMessage?.text ?: "Say hi!",
                    time = if (lastMessage != null) "Just now" else "",
                    unreadCount = unreadCount,
                    onClick = { onNavigateToChat(user.name) }
                )
            }
        }
    }
}

@Composable
fun ChatListItem(
    user: UserProfile,
    lastMessageText: String,
    time: String,
    unreadCount: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = user.avatarUrl),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = lastMessageText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (unreadCount > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = if (unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )
                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(BrandPinkLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = unreadCount.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
