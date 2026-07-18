package com.kuriamind.ui.feature.welcome

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface OnboardingStep {
    data object Welcome : OnboardingStep
    data object Permissions : OnboardingStep
}

data class PermissionItem(
    val isGranted: Boolean = false,
    val isChecked: Boolean = false,
)

data class PermissionsState(
    val postNotifications: PermissionItem = PermissionItem(),
    val notificationListener: PermissionItem = PermissionItem(),
    val accessibility: PermissionItem = PermissionItem(),
) {
    val allGranted: Boolean
        get() = postNotifications.isGranted
                && notificationListener.isGranted
                && accessibility.isGranted

    val allChecked: Boolean
        get() = postNotifications.isChecked
                && notificationListener.isChecked
                && accessibility.isChecked
}

class WelcomeViewModel : ViewModel() {
    private val _step = MutableStateFlow<OnboardingStep>(OnboardingStep.Welcome)
    val step: StateFlow<OnboardingStep> = _step.asStateFlow()

    private val _permissions = MutableStateFlow(PermissionsState())
    val permissions: StateFlow<PermissionsState> = _permissions.asStateFlow()

    fun advance() {
        _step.update { current ->
            when (current) {
                is OnboardingStep.Welcome -> OnboardingStep.Permissions
                is OnboardingStep.Permissions -> current
            }
        }
    }

    fun isLastStep(): Boolean = _step.value is OnboardingStep.Permissions

    fun updatePermission(key: String, granted: Boolean) {
        _permissions.update { state ->
            val item = PermissionItem(isGranted = granted, isChecked = true)
            when (key) {
                PERMISSION_POST_NOTIFICATIONS -> state.copy(postNotifications = item)
                PERMISSION_NOTIFICATION_LISTENER -> state.copy(notificationListener = item)
                PERMISSION_ACCESSIBILITY -> state.copy(accessibility = item)
                else -> state
            }
        }
    }
}

const val PERMISSION_POST_NOTIFICATIONS = "post_notifications"
const val PERMISSION_NOTIFICATION_LISTENER = "notification_listener"
const val PERMISSION_ACCESSIBILITY = "accessibility"
