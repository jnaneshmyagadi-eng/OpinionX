package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.BrandPinkLight
import com.example.ui.theme.BrandPurple
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
        delay(1000)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        val gradientBrush = Brush.linearGradient(
            colors = listOf(BrandPurple, BrandPinkLight, BrandOrange)
        )
        Text(
            text = "OpinionX",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Black,
                brush = gradientBrush,
                fontSize = 48.sp
            ),
            modifier = Modifier
                .alpha(alpha.value)
                .scale(scale.value)
        )
    }
}
