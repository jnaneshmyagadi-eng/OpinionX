package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MoodChip(mood: String, isSelected: Boolean = false, onClick: () -> Unit) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = mood,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}
