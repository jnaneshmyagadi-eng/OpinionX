package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
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
import coil.compose.AsyncImage
import com.example.R
import com.example.data.UserEntity
import com.example.ui.theme.BrandPinkLight
import com.example.viewmodel.OpinionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: OpinionViewModel = viewModel(),
    onClose: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    
    var username by remember { mutableStateOf(userProfile?.username ?: "me_user") }
    var displayName by remember { mutableStateOf(userProfile?.displayName ?: "My Name") }
    var bio by remember { mutableStateOf(userProfile?.bio ?: "This is my bio.") }
    var currentMood by remember { mutableStateOf(userProfile?.currentMood ?: "Happy") }
    var interests by remember { mutableStateOf(userProfile?.interests ?: "Gaming") }
    var imageUri by remember { mutableStateOf<Uri?>(userProfile?.profileImageUri?.let { Uri.parse(it) }) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
        }
    }

    val isFormValid = username.isNotBlank() && displayName.isNotBlank() && bio.length <= 150

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        if (isFormValid) {
                            val updatedUser = UserEntity(
                                id = "me",
                                username = username,
                                displayName = displayName,
                                bio = bio,
                                currentMood = currentMood,
                                interests = interests,
                                profileImageUri = imageUri?.toString()
                            )
                            viewModel.updateUserProfile(updatedUser)
                            onClose()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPinkLight),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { launcher.launch("image/*") }
                ) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Profile Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.img_avatar_1_1785475392285),
                            contentDescription = "Default Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Change Photo", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                supportingText = { Text("${bio.length}/150") }
            )

            OutlinedTextField(
                value = currentMood,
                onValueChange = { currentMood = it },
                label = { Text("Current Mood") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = interests,
                onValueChange = { interests = it },
                label = { Text("Interests") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
