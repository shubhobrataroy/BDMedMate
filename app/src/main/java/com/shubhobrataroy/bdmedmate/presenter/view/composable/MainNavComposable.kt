package com.shubhobrataroy.bdmedmate.presenter.view.composable

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavComposable() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = DashboardRoute){
        composable(DashboardRoute){ DashboardPage()}
    }
}

const val DashboardRoute = "dashboard"
