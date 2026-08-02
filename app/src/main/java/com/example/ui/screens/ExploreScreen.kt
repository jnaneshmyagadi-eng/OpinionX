package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.MoodChip
import com.example.ui.components.PollCard
import com.example.ui.components.UserProfileCard
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.BrandPinkLight
import com.example.ui.theme.BrandPurple
import com.example.viewmodel.OpinionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: OpinionViewModel = viewModel(),
    onCreatePoll: () -> Unit,
    onSayHi: (String) -> Unit,
    onNavigateToComments: (String) -> Unit
) {
    val polls by viewModel.polls.collectAsState()
    val users by viewModel.mockUsers.collectAsState()

    val allMoods = listOf(
        "😊 Happy", "😴 Bored", "😢 Sad", "🔥 Excited",
        "🤔 Thinking", "😎 Chill", "❤️ Romantic",
        "💪 Motivated", "😤 Angry", "😂 Funny"
    )
    val plainMoods = allMoods.map { it.split(" ").last() } // extract text

    var searchQuery by remember { mutableStateOf("") }
    var selectedMoodText by remember { mutableStateOf<String?>(null) }
    
    val myInterest = "Gaming" // Mocking my own interest

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            val gradientBrush = Brush.linearGradient(
                colors = listOf(BrandPurple, BrandPinkLight, BrandOrange)
            )
            Text(
                text = "Discover your vibe",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    brush = gradientBrush
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Find opinions and people who feel like you.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search opinions, moods or people...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = BrandPinkLight
                )
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                Text(
                    text = "What's your vibe right now?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allMoods) { moodEmojiText ->
                        val moodText = moodEmojiText.split(" ").last()
                        MoodChip(
                            mood = moodEmojiText,
                            isSelected = selectedMoodText == moodText,
                            onClick = {
                                selectedMoodText = if (selectedMoodText == moodText) null else moodText
                            }
                        )
                    }
                }
            }

            if (selectedMoodText != null) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Your Vibe Match",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                val matchedUsers = users.filter { it.currentMood == selectedMoodText || viewModel.calculateVibeScore(selectedMoodText!!, it.currentMood, myInterest, it.interest, it.distanceMiles) > 20 }
                
                if (matchedUsers.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No people matching this vibe yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } else {
                    items(matchedUsers, key = { it.id }) { user ->
                        val score = viewModel.calculateVibeScore(selectedMoodText!!, user.currentMood, myInterest, user.interest, user.distanceMiles)
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            UserProfileCard(
                                user = user,
                                vibeScore = score,
                                onMatch = { viewModel.toggleFollow(user.id) },
                                onSayHi = { onSayHi(user.name) }
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Opinions for this vibe",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                val filteredPolls = polls.filter { it.mood == selectedMoodText }
                
                if (filteredPolls.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "No polls for this vibe yet.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Button(
                                onClick = onCreatePoll,
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPinkLight)
                            ) {
                                Text("Create the first opinion")
                            }
                        }
                    }
                } else {
                    items(filteredPolls, key = { "poll_${it.id}" }) { poll ->
                        PollCard(
                            poll = poll,
                            onVote = { viewModel.vote(poll, it) },
                            onLike = { viewModel.toggleLike(poll) },
                            onSave = { viewModel.toggleSave(poll) },
                            onCommentClick = { onNavigateToComments(poll.id) }
                        )
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Select a mood to discover your vibe.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
