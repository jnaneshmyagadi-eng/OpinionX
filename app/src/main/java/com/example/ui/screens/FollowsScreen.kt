package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.UserProfileCard
import com.example.viewmodel.OpinionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowsScreen(
    title: String,
    viewModel: OpinionViewModel = viewModel(),
    onClose: () -> Unit,
    onSayHi: (String) -> Unit
) {
    val users by viewModel.mockUsers.collectAsState()
    
    // For MVP, just filter based on title (Followers vs Following)
    val displayUsers = if (title == "Following") {
        users.filter { it.isFollowed }
    } else {
        // Mock followers: just return a subset of users
        users.take(2)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
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
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (displayUsers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No $title yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(displayUsers, key = { it.id }) { user ->
                    UserProfileCard(
                        user = user,
                        vibeScore = viewModel.calculateVibeScore(
                            userMood = "Happy", 
                            targetMood = user.currentMood, 
                            userInterest = "Gaming", 
                            targetInterest = user.interest, 
                            distance = user.distanceMiles
                        ),
                        onMatch = { viewModel.toggleFollow(user.id) },
                        onSayHi = { onSayHi(user.name) }
                    )
                }
            }
        }
    }
}
