package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.data.CommentEntity
import com.example.ui.theme.BrandPinkLight
import com.example.viewmodel.OpinionViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    pollId: String,
    viewModel: OpinionViewModel = viewModel(),
    onClose: () -> Unit
) {
    val poll = viewModel.polls.collectAsState().value.find { it.id == pollId }
    val comments = viewModel.getCommentsForPoll(pollId).collectAsState(initial = emptyList())
    var commentText by remember { mutableStateOf("") }
    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comments", fontWeight = FontWeight.Bold) },
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
                    value = commentText,
                    onValueChange = { commentText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Add a comment...") },
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            val newComment = CommentEntity(
                                id = UUID.randomUUID().toString(),
                                pollId = pollId,
                                username = userProfile?.username ?: "me_user",
                                text = commentText,
                                timestamp = System.currentTimeMillis()
                            )
                            viewModel.addComment(pollId, newComment)
                            commentText = ""
                        }
                    },
                    modifier = Modifier
                        .background(BrandPinkLight, RoundedCornerShape(24.dp))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (poll != null) {
                Text(
                    text = poll.question,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            }
            
            if (comments.value.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No comments yet. Be the first to share your opinion.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(comments.value, key = { it.id }) { comment ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_avatar_1_1785475392285),
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "@${comment.username}",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = comment.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
