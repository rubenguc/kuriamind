package com.kuriamind.ui.feature.welcome

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import com.kuriamind.R
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
    var showAccessibilityPrivacyDialog by remember { mutableStateOf(false) }

    // POST_NOTIFICATIONS (API 33+)
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            viewModel.updatePermission(PERMISSION_POST_NOTIFICATIONS, granted)
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

    // Accessibility privacy dialog (Play Store policy)
    if (showAccessibilityPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showAccessibilityPrivacyDialog = false },
            title = { Text(stringResource(R.string.accessibility_privacy_title)) },
            text = { Text(stringResource(R.string.accessibility_privacy_body)) },
            confirmButton = {
                TextButton(onClick = {
                    showAccessibilityPrivacyDialog = false
                    accessibilityLauncher.launch(
                        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS),
                    )
                }) {
                    Text(stringResource(R.string.accessibility_privacy_agree))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAccessibilityPrivacyDialog = false }) {
                    Text(stringResource(R.string.accessibility_privacy_cancel))
                }
            },
        )
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
                PERMISSION_NOTIFICATION_LISTENER -> {
                    notificationListenerLauncher.launch(
                        Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS),
                    )
                }
                PERMISSION_ACCESSIBILITY -> {
                    showAccessibilityPrivacyDialog = true
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
    val enabledPackages =  NotificationManagerCompat.getEnabledListenerPackages(context)
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
