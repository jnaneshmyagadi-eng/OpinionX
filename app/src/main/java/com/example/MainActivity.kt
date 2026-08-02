package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.BottomNavigationBar
import com.example.ui.screens.CreatePollScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.FollowsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.CommentsScreen
import com.example.ui.screens.EditProfileScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ChatListScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                OpinionXApp()
            }
        }
    }
}

@Composable
fun OpinionXApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "splash"
    val showBottomNav = currentRoute in listOf("home", "explore", "create", "chat", "profile")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomNav) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("home") { 
                HomeScreen(
                    onNavigateToNotifications = { navController.navigate("notifications") },
                    onNavigateToComments = { pollId -> navController.navigate("comments/$pollId") }
                ) 
            }
            composable("explore") { 
                ExploreScreen(
                    onCreatePoll = { navController.navigate("create") },
                    onSayHi = { userName -> navController.navigate("chat/$userName") },
                    onNavigateToComments = { pollId -> navController.navigate("comments/$pollId") }
                ) 
            }
            composable("create") { 
                CreatePollScreen(
                    onPublish = { 
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onClose = { navController.popBackStack() }
                ) 
            }
            composable("chat") { ChatListScreen(onNavigateToChat = { userName -> navController.navigate("chat/$userName") }, onNavigateToNotifications = { navController.navigate("notifications") }) }
            composable("notifications") { NotificationsScreen(onClose = { navController.popBackStack() }, onNavigateToChat = { userName -> navController.navigate("chat/$userName") }) }
            composable("chat/{userName}") { backStackEntry ->
                val userName = backStackEntry.arguments?.getString("userName")
                ChatScreen(userName = userName, onClose = { navController.popBackStack() })
            }
            composable("profile") { 
                ProfileScreen(
                    onEditProfile = { navController.navigate("editProfile") },
                    onNavigateToSaved = { navController.navigate("saved") },
                    onNavigateToFollows = { title -> navController.navigate("follows/$title") },
                    onCreatePoll = { navController.navigate("create") },
                    onNavigateToComments = { pollId -> navController.navigate("comments/$pollId") }
                ) 
            }
            composable("editProfile") {
                EditProfileScreen(onClose = { navController.popBackStack() })
            }
            composable("saved") {
                SavedScreen(
                    onClose = { navController.popBackStack() },
                    onNavigateToComments = { pollId -> navController.navigate("comments/$pollId") }
                )
            }
            composable("follows/{title}") { backStackEntry ->
                val title = backStackEntry.arguments?.getString("title") ?: "Follows"
                FollowsScreen(
                    title = title,
                    onClose = { navController.popBackStack() },
                    onSayHi = { userName -> navController.navigate("chat/$userName") }
                )
            }
            composable("comments/{pollId}") { backStackEntry ->
                val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
                CommentsScreen(pollId = pollId, onClose = { navController.popBackStack() })
            }
        }
    }
}

