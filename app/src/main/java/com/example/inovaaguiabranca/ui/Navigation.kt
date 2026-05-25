package com.example.inovaaguiabranca.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inovaaguiabranca.viewmodel.AuthViewModel
import com.example.inovaaguiabranca.viewmodel.InnovationViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel, innovationViewModel: InnovationViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(authViewModel = authViewModel, onLoginSuccess = { role ->
                val destination = when (role) {
                    "MANAGER" -> "manager"
                    "LEADER" -> "leader"
                    else -> "operator"
                }
                navController.navigate(destination) {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("operator") {
            OperatorScreen(authViewModel, innovationViewModel, onLogout = {
                navController.navigate("login") { popUpTo(0) }
            })
        }
        composable("manager") {
            ManagerScreen(authViewModel, innovationViewModel, onLogout = {
                navController.navigate("login") { popUpTo(0) }
            })
        }
        composable("leader") {
            LeaderScreen(authViewModel, innovationViewModel, onLogout = {
                navController.navigate("login") { popUpTo(0) }
            }, onNavigateToProject = { projectId ->
                navController.navigate("project_detail/$projectId")
            })
        }
        composable("project_detail/{projectId}") { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
            ProjectDetailScreen(
                projectId = projectId,
                innovationViewModel = innovationViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
