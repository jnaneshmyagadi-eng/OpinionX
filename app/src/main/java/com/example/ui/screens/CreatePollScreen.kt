package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.model.Poll
import com.example.ui.components.MoodChip
import com.example.ui.components.PollCard
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.BrandPinkLight
import com.example.ui.theme.BrandPurple
import com.example.viewmodel.OpinionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePollScreen(
    viewModel: OpinionViewModel = viewModel(),
    onPublish: () -> Unit,
    onClose: () -> Unit
) {
    var question by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    
    val moods = listOf("Happy", "Bored", "Sad", "Excited", "Thinking")
    var selectedMood by remember { mutableStateOf(moods.first()) }
    
    var imageUriA by remember { mutableStateOf<Uri?>(null) }
    var imageUriB by remember { mutableStateOf<Uri?>(null) }

    val isFormValid = question.isNotBlank() && optionA.isNotBlank() && optionB.isNotBlank()

    val launcherA = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUriA = uri
    }
    val launcherB = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUriB = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Poll", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        if (isFormValid) {
                            viewModel.createPoll(
                                question = question,
                                optionA = optionA,
                                optionB = optionB,
                                imageUriA = imageUriA?.toString(),
                                imageUriB = imageUriB?.toString(),
                                mood = selectedMood
                            )
                            onPublish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandPinkLight
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Publish", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "What's on your mind?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            OutlinedTextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("Question") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            if (question.isBlank()) {
                Text("Question cannot be empty", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Text("Options", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            OutlinedTextField(
                value = optionA,
                onValueChange = { optionA = it },
                label = { Text("Option A") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { launcherA.launch("image/*") }) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Add Image A", tint = if (imageUriA != null) BrandPinkLight else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
            if (optionA.isBlank()) {
                Text("Option A cannot be empty", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = optionB,
                onValueChange = { optionB = it },
                label = { Text("Option B") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { launcherB.launch("image/*") }) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Add Image B", tint = if (imageUriB != null) BrandPinkLight else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
            if (optionB.isBlank()) {
                Text("Option B cannot be empty", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Text("Mood", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            // Mood chips using wrapping row (or simply scrollable row)
            androidx.compose.foundation.lazy.LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(moods.size) { index ->
                    val mood = moods[index]
                    MoodChip(
                        mood = mood,
                        isSelected = selectedMood == mood,
                        onClick = { selectedMood = mood }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Live Preview", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            val previewPoll = Poll(
                id = "preview",
                username = "me_user",
                avatarUrl = R.drawable.img_avatar_1_1785475392285,
                timeAgo = "Just now",
                question = question.ifBlank { "Your question here?" },
                optionA = optionA.ifBlank { "Option A" },
                optionB = optionB.ifBlank { "Option B" },
                optionAImageUri = imageUriA?.toString(),
                optionBImageUri = imageUriB?.toString(),
                votesA = 0,
                votesB = 0,
                hasVoted = false,
                likes = 0,
                comments = 0
            )
            
            PollCard(
                poll = previewPoll,
                onVote = {} // Disabled for preview
            )
            
            Spacer(modifier = Modifier.height(40.dp)) // Extra padding for bottom bar
        }
    }
}
