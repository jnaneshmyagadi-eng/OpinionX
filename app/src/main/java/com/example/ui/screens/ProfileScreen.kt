package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.PollCard
import com.example.ui.theme.BrandPinkLight
import com.example.ui.theme.BrandPurple
import com.example.viewmodel.OpinionViewModel

@Composable
fun ProfileScreen(
    viewModel: OpinionViewModel = viewModel(),
    onEditProfile: () -> Unit,
    onNavigateToSaved: () -> Unit,
    onNavigateToFollows: (String) -> Unit,
    onCreatePoll: () -> Unit,
    onNavigateToComments: (String) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val allPolls by viewModel.polls.collectAsState()
    val myPolls = allPolls.filter { it.username == (userProfile?.username ?: "me_user") }
    val myActivity = allPolls.filter { it.hasVoted }
    val follows by viewModel.follows.collectAsState()
    
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onNavigateToSaved) {
                    Icon(Icons.Default.Bookmark, contentDescription = "Saved")
                }
                IconButton(onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out my OpinionX profile: @${userProfile?.username ?: "me_user"}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Profile"))
                }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            ) {
                if (userProfile?.profileImageUri != null) {
                    AsyncImage(
                        model = userProfile?.profileImageUri,
                        contentDescription = "Profile Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.img_avatar_1_1785475392285),
                        contentDescription = "Default Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userProfile?.displayName ?: "My Name",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "@${userProfile?.username ?: "me_user"}",
                style = MaterialTheme.typography.bodyMedium,
                color = BrandPinkLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = userProfile?.bio ?: "This is my bio.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${myPolls.size}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "Polls", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigateToFollows("Followers") }
                ) {
                    Text(text = "120", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "Followers", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigateToFollows("Following") }
                ) {
                    Text(text = "${follows.size}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = "Following", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onEditProfile,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Profile", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile", fontWeight = FontWeight.Bold)
            }
        }

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = BrandPinkLight
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("My Polls", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold, color = if (selectedTab == 0) BrandPinkLight else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("My Activity", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold, color = if (selectedTab == 1) BrandPinkLight else MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            val listToShow = if (selectedTab == 0) myPolls else myActivity

            if (listToShow.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            if (selectedTab == 0) "You haven't created any opinions yet." else "No activity yet.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        if (selectedTab == 0) {
                            Button(
                                onClick = onCreatePoll,
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
                            ) {
                                Text("Create your first opinion")
                            }
                        }
                    }
                }
            } else {
                items(listToShow, key = { "profile_${selectedTab}_${it.id}" }) { poll ->
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
