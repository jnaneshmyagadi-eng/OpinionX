package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.data.NotificationEntity
import com.example.ui.theme.BrandPinkLight
import com.example.viewmodel.OpinionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: OpinionViewModel = viewModel(),
    onClose: () -> Unit,
    onNavigateToChat: (String) -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.markAllNotificationsAsRead() }) {
                        Icon(Icons.Default.DoneAll, contentDescription = "Mark all read", tint = BrandPinkLight)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No notifications yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(notifications, key = { it.id }) { notif ->
                    NotificationItem(
                        notification = notif,
                        onClick = {
                            viewModel.markNotificationAsRead(notif.id)
                            if (notif.type == "Message") {
                                onNavigateToChat(notif.actorUsername)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationEntity, onClick: () -> Unit) {
    val backgroundColor = if (notification.isRead) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_avatar_1_1785475392285), // default avatar
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.text,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Just now", // MVP
                style = MaterialTheme.typography.labelSmall,
                color = BrandPinkLight
            )
        }
        if (!notification.isRead) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(BrandPinkLight, CircleShape)
            )
        }
    }
}
