package com.kuriamind.ui.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.kuriamind.ui.feature.blocks.BlocksScreen
import com.kuriamind.ui.feature.settings.SettingsScreen
import com.kuriamind.ui.feature.stats.StatsScreen

@Composable
fun MainScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1a1a1a),
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Home, "Blocks") },
                    label = { Text("Blocks") },
                    colors = bottomNavColors(),
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.BarChart, "Stats") },
                    label = { Text("Stats") },
                    colors = bottomNavColors(),
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Settings, "Settings") },
                    label = { Text("Settings") },
                    colors = bottomNavColors(),
                )
            }
        },
    ) {
        when (selectedTab) {
            0 -> BlocksScreen()
            1 -> StatsScreen()
            2 -> SettingsScreen()
        }
    }
}

@Composable
private fun bottomNavColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color(0xFF1D71B8),
    selectedTextColor = Color(0xFF1D71B8),
    unselectedIconColor = Color.Gray,
    unselectedTextColor = Color.Gray,
    indicatorColor = Color.Transparent,
)
