package com.kuriamind.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuriamind.ui.feature.blocks.BlockScreen
import com.kuriamind.ui.feature.main.MainScreen
import com.kuriamind.ui.feature.welcome.WelcomeScreen

private const val PREFS_NAME = "kuriamind_prefs"
private const val KEY_FIRST_TIME = "is_first_time"

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: Any,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Welcome> {
            val prefs = LocalContext.current
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

            WelcomeScreen(
                onFinished = {
                    prefs.edit().putBoolean(KEY_FIRST_TIME, false).apply()
                    navController.navigate(Main) {
                        popUpTo<Welcome> { inclusive = true }
                    }
                },
            )
        }

        composable<Main> {
            MainScreen(
                onNavigateToBlock = { blockId ->
                    navController.navigate(Block(blockId = blockId ?: ""))
                },
            )
        }

        composable<Block> {
            BlockScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
