package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.*
import com.example.viewmodel.OpinionViewModel

@Composable
fun OpinionXTopBar(
    modifier: Modifier = Modifier,
    viewModel: OpinionViewModel = viewModel(),
    onNotificationsClick: () -> Unit = {}
) {
    val unreadCount by viewModel.unreadNotificationCount.collectAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val gradientBrush = Brush.linearGradient(
            colors = listOf(BrandPurple, BrandPinkLight, BrandOrange)
        )
        Text(
            text = "OpinionX",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                brush = gradientBrush,
                fontSize = 24.sp
            )
        )
        
        Box {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            if (unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(BrandPinkLight, CircleShape)
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
