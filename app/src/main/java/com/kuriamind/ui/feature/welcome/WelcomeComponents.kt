package com.kuriamind.ui.feature.welcome

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuriamind.R
import com.kuriamind.ui.theme.DarkSurfaceContainer
import com.kuriamind.ui.theme.StatusGreen

@Composable
fun WelcomeScreenContent(
    step: OnboardingStep,
    permissions: PermissionsState,
    onNext: () -> Unit,
    onRequestPermission: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary, Color.Black),
                ),
            ),
    ) {
        Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
            AnimatedContent(
                targetState = step,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                transitionSpec = {
                    val direction = if (targetState is OnboardingStep.Permissions) 1 else -1
                    (slideInHorizontally { width -> direction * width / 4 } + fadeIn(tween(300)))
                        .togetherWith(
                            slideOutHorizontally { width -> -direction * width / 4 } + fadeOut(tween(200)),
                        )
                },
                label = "onboarding",
            ) { currentStep ->
                when (currentStep) {
                    is OnboardingStep.Welcome -> WelcomeContent()
                    is OnboardingStep.Permissions -> PermissionsContent(
                        permissions = permissions,
                        onRequestPermission = onRequestPermission,
                    )
                }
            }

            Box(modifier = Modifier.padding(10.dp)) {
                FilledTonalButton(
                    onClick = onNext,
                    enabled = step !is OnboardingStep.Permissions || permissions.allGranted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = stringResource(
                            when (step) {
                                is OnboardingStep.Welcome -> R.string.next
                                is OnboardingStep.Permissions -> R.string.start_app
                            },
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(120.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_app_icon),
                contentDescription = stringResource(R.string.app_icon_description),
                modifier = Modifier.size(180.dp),
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(R.string.welcome_to),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Text(
                text = stringResource(R.string.kuria_mind),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.welcome_description_1),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                lineHeight = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Start,
            )

            Text(
                text = stringResource(R.string.welcome_description_2),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun PermissionsContent(
    permissions: PermissionsState,
    onRequestPermission: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(R.string.permissions_message),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            lineHeight = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 24.dp),
            textAlign = TextAlign.Start,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PermissionCard(
                icon = Icons.Filled.Notifications,
                title = stringResource(R.string.permission_post_notifications_title),
                description = stringResource(R.string.permission_post_notifications_description),
                isActive = permissions.postNotifications.isGranted,
                isChecked = permissions.postNotifications.isChecked,
                onClick = { onRequestPermission(PERMISSION_POST_NOTIFICATIONS) },
            )
            PermissionCard(
                icon = Icons.Filled.Notifications,
                title = stringResource(R.string.permission_notification_listener_title),
                description = stringResource(R.string.permission_notification_listener_description),
                isActive = permissions.notificationListener.isGranted,
                isChecked = permissions.notificationListener.isChecked,
                onClick = { onRequestPermission(PERMISSION_NOTIFICATION_LISTENER) },
            )
            PermissionCard(
                icon = Icons.Filled.Accessibility,
                title = stringResource(R.string.permission_accessibility_title),
                description = stringResource(R.string.permission_accessibility_description),
                isActive = permissions.accessibility.isGranted,
                isChecked = permissions.accessibility.isChecked,
                onClick = { onRequestPermission(PERMISSION_ACCESSIBILITY) },
            )
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun PermissionCard(
    icon: ImageVector,
    title: String,
    description: String,
    isActive: Boolean,
    isChecked: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (isActive) StatusGreen else MaterialTheme.colorScheme.outline
    val checkTint = if (isActive) StatusGreen else MaterialTheme.colorScheme.outline

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = !isActive, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer),
        border = BorderStroke(2.dp, borderColor),
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
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = checkTint,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    lineHeight = 20.sp,
                )
            }
        }
    }
}
