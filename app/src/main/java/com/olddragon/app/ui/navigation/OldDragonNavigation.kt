package com.olddragon.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.olddragon.app.ui.screens.CharacterCreationScreen
import com.olddragon.app.ui.screens.MainMenuScreen

@Composable
fun OldDragonNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_menu"
    ) {
        composable("main_menu") {
            MainMenuScreen(
                onCreateCharacterClick = {
                    navController.navigate("character_creation")
                }
            )
        }

        composable("character_creation") {
            CharacterCreationScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}