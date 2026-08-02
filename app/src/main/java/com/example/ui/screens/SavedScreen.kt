package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.PollCard
import com.example.ui.theme.BrandPinkLight
import com.example.viewmodel.OpinionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    viewModel: OpinionViewModel = viewModel(),
    onClose: () -> Unit,
    onNavigateToComments: (String) -> Unit
) {
    val polls by viewModel.polls.collectAsState()
    val savedPolls = polls.filter { it.isSaved }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Opinions", fontWeight = FontWeight.Bold) },
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
        if (savedPolls.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No saved opinions yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(savedPolls, key = { "saved_${it.id}" }) { poll ->
                    PollCard(
                        poll = poll,
                        onVote = { viewModel.vote(poll, it) },
                        onLike = { viewModel.toggleLike(poll) },
                        onSave = { viewModel.toggleSave(poll) },
                        onCommentClick = { onNavigateToComments(poll.id) }
                    )
                }
            }
        }
    }
}
