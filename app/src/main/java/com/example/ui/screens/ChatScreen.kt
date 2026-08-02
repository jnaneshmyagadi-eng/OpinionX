package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.MessageEntity
import com.example.model.UserProfile
import com.example.ui.theme.BrandPinkLight
import com.example.ui.theme.BrandPurple
import com.example.viewmodel.OpinionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userName: String?,
    viewModel: OpinionViewModel = viewModel(),
    onClose: () -> Unit
) {
    val users by viewModel.mockUsers.collectAsState()
    val targetUser = users.find { it.name.equals(userName, ignoreCase = true) }
    
    val myProfile by viewModel.userProfile.collectAsState()
    val myId = "me"
    val myUsername = myProfile?.username ?: "me_user"
    
    val targetId = targetUser?.id ?: "u1"
    val targetUsername = targetUser?.name ?: "Unknown"

    val messages by viewModel.getMessagesBetweenUsers(myId, targetId).collectAsState(initial = emptyList())
    var inputText by remember { mutableStateOf("") }
    
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
    
    LaunchedEffect(Unit) {
        viewModel.markMessagesAsRead(myId, targetId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (targetUser != null) {
                            Image(
                                painter = painterResource(id = targetUser.avatarUrl),
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        Text(targetUsername, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Message...") },
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendMessage(
                                senderId = myId,
                                receiverId = targetId,
                                senderUsername = myUsername,
                                receiverUsername = targetUsername,
                                text = inputText
                            )
                            inputText = ""
                        }
                    },
                    modifier = Modifier.background(BrandPinkLight, RoundedCornerShape(24.dp))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { innerPadding ->
        if (messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Say hi to $targetUsername!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    val isMe = msg.senderId == myId
                    MessageBubble(msg = msg, isMe = isMe)
                }
            }
        }
    }
}

@Composable
fun MessageBubble(msg: MessageEntity, isMe: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isMe) BrandPurple else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = msg.text,
                color = if (isMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
