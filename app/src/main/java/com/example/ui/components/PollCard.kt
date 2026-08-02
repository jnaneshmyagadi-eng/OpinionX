package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import com.example.model.Poll
import com.example.ui.theme.*

@Composable
fun PollCard(
    poll: Poll,
    onVote: (String) -> Unit,
    onLike: () -> Unit = {},
    onSave: () -> Unit = {},
    onCommentClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Avatar, Username, Time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = poll.avatarUrl),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "@${poll.username}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = poll.timeAgo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question
            Text(
                text = poll.question,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 28.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Voting Options
            VoteOption(
                text = poll.optionA,
                isSelected = poll.selectedOption == "A",
                showResults = poll.hasVoted,
                percentage = poll.percentA(),
                onClick = { onVote("A") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            VoteOption(
                text = poll.optionB,
                isSelected = poll.selectedOption == "B",
                showResults = poll.hasVoted,
                percentage = poll.percentB(),
                onClick = { onVote("B") }
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            // Footer: Votes count, Like, Comment
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${poll.totalVotes} votes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onLike) {
                        Icon(
                            imageVector = if (poll.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (poll.isLiked) BrandPinkLight else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${poll.likes}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onCommentClick) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comment",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${poll.comments}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onSave) {
                        Icon(
                            imageVector = if (poll.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (poll.isSaved) BrandPurple else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VoteOption(
    text: String,
    isSelected: Boolean,
    showResults: Boolean,
    percentage: Float,
    onClick: () -> Unit
) {
    val animatedPercent by animateFloatAsState(
        targetValue = if (showResults) percentage else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "percentage_anim"
    )

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(BrandPurple, BrandPinkDark)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isSelected && !showResults) 2.dp else 1.dp,
                brush = if (isSelected) gradientBrush else Brush.horizontalGradient(
                    listOf(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f), MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = !showResults) { onClick() }
    ) {
        // Results background bar
        if (showResults) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedPercent)
                    .background(
                        brush = if (isSelected) gradientBrush else Brush.horizontalGradient(
                            listOf(Color.DarkGray, Color.DarkGray)
                        )
                    )
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (showResults) {
                Text(
                    text = "${(animatedPercent * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
