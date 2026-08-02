package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.BrandPinkLight

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        val items = listOf("Home", "Explore", "Create", "Chat", "Profile")
        val selectedColor = BrandPinkLight
        val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

        items.forEach { item ->
            val isSelected = currentRoute.equals(item, ignoreCase = true)
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.lowercase()) },
                icon = {
                    when (item) {
                        "Home" -> Icon(if (isSelected) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "Home")
                        "Explore" -> Icon(if (isSelected) Icons.Filled.Search else Icons.Outlined.Search, contentDescription = "Explore")
                        "Create" -> Icon(Icons.Filled.AddCircle, contentDescription = "Create", tint = BrandPinkLight)
                        "Chat" -> Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Chat")
                        "Profile" -> Icon(if (isSelected) Icons.Filled.Person else Icons.Outlined.Person, contentDescription = "Profile")
                    }
                },
                label = { Text(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    selectedTextColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
