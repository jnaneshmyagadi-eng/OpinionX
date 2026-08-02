package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.MoodChip
import com.example.ui.components.OpinionXTopBar
import com.example.ui.components.PollCard
import com.example.viewmodel.OpinionViewModel

@Composable
fun HomeScreen(
    viewModel: OpinionViewModel = viewModel(),
    onNavigateToComments: (String) -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val polls by viewModel.polls.collectAsState()
    
    val moods = listOf("Happy", "Bored", "Sad", "Excited", "Thinking")
    var selectedMood by remember { mutableStateOf(moods.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OpinionXTopBar(onNotificationsClick = onNavigateToNotifications)

        // Mood Chips Row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(moods) { mood ->
                MoodChip(
                    mood = mood,
                    isSelected = selectedMood == mood,
                    onClick = { selectedMood = mood }
                )
            }
        }

        // Feed
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp) // Space for bottom nav
        ) {
            items(
                items = polls,
                key = { it.id }
            ) { poll ->
                PollCard(
                    poll = poll,
                    onVote = { option -> viewModel.vote(poll, option) },
                    onLike = { viewModel.toggleLike(poll) },
                    onSave = { viewModel.toggleSave(poll) },
                    onCommentClick = { onNavigateToComments(poll.id) }
                )
            }
        }
    }
}
