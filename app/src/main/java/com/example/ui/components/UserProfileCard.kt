package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.UserProfile
import com.example.ui.theme.*

@Composable
fun UserProfileCard(
    user: UserProfile,
    vibeScore: Int,
    onMatch: () -> Unit,
    onSayHi: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = user.avatarUrl),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${user.name}, ${user.age}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${user.distanceMiles} miles away",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandPinkLight.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "$vibeScore% Match",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = BrandPinkLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MoodChip(mood = user.currentMood, isSelected = true, onClick = {})
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.interest,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (user.isFollowed) {
                    Button(
                        onClick = onMatch,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Following ✓", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = onMatch,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPinkLight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Follow", fontWeight = FontWeight.Bold)
                    }
                }
                Button(
                    onClick = onSayHi,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Say Hi 👋", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
