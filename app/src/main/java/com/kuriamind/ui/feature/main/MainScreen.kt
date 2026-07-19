package com.kuriamind.ui.feature.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.kuriamind.ui.feature.blocks.BlocksScreen
import com.kuriamind.ui.feature.settings.SettingsScreen
import com.kuriamind.ui.feature.stats.StatsScreen
import com.kuriamind.ui.theme.DarkSurfaceContainer

@Composable
fun MainScreen(
    onNavigateToBlock: (blockId: String?) -> Unit = {},
) {
    val context = LocalContext.current
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var missingPermissions by remember { mutableStateOf(emptyList<PermissionType>()) }
    var showAccessibilityPrivacyDialog by remember { mutableStateOf(false) }

    // Permission launchers
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { checkMissingPermissions(context) { missingPermissions = it } },
    )

    val notificationListenerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { checkMissingPermissions(context) { missingPermissions = it } },
    )

    val accessibilityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { checkMissingPermissions(context) { missingPermissions = it } },
    )

    // Check permissions on every resume (app opens / comes from background)
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            checkMissingPermissions(context) { missingPermissions = it }
        }
    }

    // Accessibility privacy dialog (Play Store policy)
    if (showAccessibilityPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showAccessibilityPrivacyDialog = false },
            title = { Text("A note on privacy") },
            text = {
                Text("By enabling the accessibility service, you agree that Focus: Kuria Mind can monitor which apps you open in order to block selected ones and log their usage locally. Additionally, it will access system interfaces to provide functionalities such as strict mode.\n\nRest assured, we will never access or share any of your personal data during this process.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showAccessibilityPrivacyDialog = false
                    accessibilityLauncher.launch(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                }) { Text("Agree") }
            },
            dismissButton = {
                TextButton(onClick = { showAccessibilityPrivacyDialog = false }) { Text("Cancel") }
            },
        )
    }

    // Permissions missing dialog
    if (missingPermissions.isNotEmpty()) {
        MissingPermissionsDialog(
            missingPermissions = missingPermissions,
            onDismiss = { missingPermissions = emptyList() },
            onRequestPermission = { permission ->
                when (permission) {
                    PermissionType.POST_NOTIFICATIONS -> {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    PermissionType.NOTIFICATION_LISTENER -> {
                        notificationListenerLauncher.launch(
                            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS),
                        )
                    }
                    PermissionType.ACCESSIBILITY -> {
                        showAccessibilityPrivacyDialog = true
                    }
                }
            },
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(120.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = NavigationBarDefaults.Elevation,
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Blocks",
                            modifier = Modifier.size(20.dp),
                        )
                    },
                    label = { Text("Blocks") },
                    colors = bottomNavItemColors(),
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            Icons.Filled.BarChart,
                            contentDescription = "Stats",
                            modifier = Modifier.size(20.dp),
                        )
                    },
                    label = { Text("Stats") },
                    colors = bottomNavItemColors(),
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(20.dp),
                        )
                    },
                    label = { Text("Settings") },
                    colors = bottomNavItemColors(),
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (selectedTab) {
                0 -> BlocksScreen(
                    onAddBlock = { onNavigateToBlock(null) },
                    onEditBlock = { blockId -> onNavigateToBlock(blockId) },
                )
                1 -> StatsScreen()
                2 -> SettingsScreen()
            }
        }
    }
}

// ─── Permission types ─────────────────────────────────────────────

private enum class PermissionType {
    POST_NOTIFICATIONS,
    NOTIFICATION_LISTENER,
    ACCESSIBILITY,
}

private data class PermissionInfo(
    val type: PermissionType,
    val icon: ImageVector,
    val title: String,
    val description: String,
)

private fun permissionInfo(type: PermissionType): PermissionInfo = when (type) {
    PermissionType.POST_NOTIFICATIONS -> PermissionInfo(
        type = type,
        icon = Icons.Filled.Notifications,
        title = "Post Notifications",
        description = "Allow Kuria Mind to notify you when an app is blocked",
    )
    PermissionType.NOTIFICATION_LISTENER -> PermissionInfo(
        type = type,
        icon = Icons.Filled.Notifications,
        title = "Notification Listener",
        description = "Required to automatically dismiss notifications from blocked apps",
    )
    PermissionType.ACCESSIBILITY -> PermissionInfo(
        type = type,
        icon = Icons.Filled.Accessibility,
        title = "Accessibility Services",
        description = "Required to detect and block apps you want to restrict",
    )
}

// ─── Missing Permissions Dialog ────────────────────────────────────

@Composable
private fun MissingPermissionsDialog(
    missingPermissions: List<PermissionType>,
    onDismiss: () -> Unit,
    onRequestPermission: (PermissionType) -> Unit,
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = com.kuriamind.ui.theme.DarkSurfaceContainer),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
            ) {
                Text(
                    text = "Permissions Required",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Some permissions needed for blocking apps are disabled. Please enable them to keep Kuria Mind working correctly.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    lineHeight = 20.sp,
                )

                Spacer(Modifier.height(20.dp))

                missingPermissions.forEach { permission ->
                    val info = permissionInfo(permission)
                    PermissionCard(
                        icon = info.icon,
                        title = info.title,
                        description = info.description,
                        onClick = { onRequestPermission(permission) },
                    )
                    Spacer(Modifier.height(12.dp))
                }

                Spacer(Modifier.height(24.dp))

                FilledTonalButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.outlineVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Text("Maybe Later", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun PermissionCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    lineHeight = 20.sp,
                )
            }
            Text(
                text = "Enable",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

// ─── Permission check helpers ──────────────────────────────────────

private fun checkMissingPermissions(
    context: Context,
    onResult: (List<PermissionType>) -> Unit,
) {
    val missing = mutableListOf<PermissionType>()

    // POST_NOTIFICATIONS (API 33+)
    if (Build.VERSION.SDK_INT >= 33) {
        val granted = NotificationManagerCompat.from(context).areNotificationsEnabled()
        if (!granted) missing.add(PermissionType.POST_NOTIFICATIONS)
    }

    // NOTIFICATION_LISTENER
    val enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(context)
    if (!enabledPackages.contains(context.packageName)) {
        missing.add(PermissionType.NOTIFICATION_LISTENER)
    }

    // ACCESSIBILITY
    val enabledServices = try {
        Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
        ) ?: ""
    } catch (_: Exception) {
        ""
    }
    if (!enabledServices.contains(context.packageName)) {
        missing.add(PermissionType.ACCESSIBILITY)
    }

    onResult(missing)
}

// ─── Bottom nav colors ─────────────────────────────────────────────

@Composable
private fun bottomNavItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
    indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
)
