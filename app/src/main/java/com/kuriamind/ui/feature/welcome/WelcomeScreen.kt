package com.kuriamind.ui.feature.welcome

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WelcomeScreen(
    onFinished: () -> Unit,
    viewModel: WelcomeViewModel = viewModel(),
) {
    val context = LocalContext.current
    val step by viewModel.step.collectAsStateWithLifecycle()
    val permissions by viewModel.permissions.collectAsStateWithLifecycle()

    // POST_NOTIFICATIONS (API 33+)
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            viewModel.updatePermission(PERMISSION_POST_NOTIFICATIONS, granted)
        },
    )

    // SYSTEM_ALERT_WINDOW
    val overlayLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            viewModel.updatePermission(
                PERMISSION_DISPLAY_OVERLAY,
                Settings.canDrawOverlays(context),
            )
        },
    )

    // NOTIFICATION_LISTENER
    val notificationListenerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            viewModel.updatePermission(
                PERMISSION_NOTIFICATION_LISTENER,
                isNotificationListenerServiceEnabled(context),
            )
        },
    )

    // ACCESSIBILITY
    val accessibilityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            viewModel.updatePermission(
                PERMISSION_ACCESSIBILITY,
                isAccessibilityServiceEnabled(context),
            )
        },
    )

    // Check permission states when entering the Permissions step
    LaunchedEffect(step) {
        if (step is OnboardingStep.Permissions) {
            checkAllPermissions(context, viewModel)
        }
    }

    WelcomeScreenContent(
        step = step,
        permissions = permissions,
        onNext = {
            if (viewModel.isLastStep()) {
                onFinished()
            } else {
                viewModel.advance()
            }
        },
        onRequestPermission = { permissionKey ->
            when (permissionKey) {
                PERMISSION_POST_NOTIFICATIONS -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                PERMISSION_DISPLAY_OVERLAY -> {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}"),
                    )
                    overlayLauncher.launch(intent)
                }
                PERMISSION_NOTIFICATION_LISTENER -> {
                    notificationListenerLauncher.launch(
                        Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS),
                    )
                }
                PERMISSION_ACCESSIBILITY -> {
                    accessibilityLauncher.launch(
                        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS),
                    )
                }
            }
        },
    )
}

private fun checkAllPermissions(context: Context, viewModel: WelcomeViewModel) {
    // POST_NOTIFICATIONS
    if (Build.VERSION.SDK_INT >= 33) {
        val granted = NotificationManagerCompat.from(context)
            .areNotificationsEnabled()
        viewModel.updatePermission(PERMISSION_POST_NOTIFICATIONS, granted)
    } else {
        viewModel.updatePermission(PERMISSION_POST_NOTIFICATIONS, true)
    }

    // SYSTEM_ALERT_WINDOW
    viewModel.updatePermission(
        PERMISSION_DISPLAY_OVERLAY,
        Settings.canDrawOverlays(context),
    )

    // NOTIFICATION_LISTENER
    viewModel.updatePermission(
        PERMISSION_NOTIFICATION_LISTENER,
        isNotificationListenerServiceEnabled(context),
    )

    // ACCESSIBILITY
    viewModel.updatePermission(
        PERMISSION_ACCESSIBILITY,
        isAccessibilityServiceEnabled(context),
    )
}

private fun isNotificationListenerServiceEnabled(context: Context): Boolean {
    val packageName = context.packageName
    val enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(context)
    return enabledPackages.contains(packageName)
}

private fun isAccessibilityServiceEnabled(context: Context): Boolean {
    val packageName = context.packageName
    val enabledServices = try {
        Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
        ) ?: ""
    } catch (_: Exception) {
        ""
    }
    return enabledServices.contains(packageName)
}
