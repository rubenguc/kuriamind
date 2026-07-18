package com.kuriamind.services

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.view.accessibility.AccessibilityEvent

/**
 * Stub AccessibilityService required for BIND_ACCESSIBILITY_SERVICE.
 * Full implementation will follow in a future phase.
 */
@SuppressLint("AccessibilityPolicy")
class AppMonitorService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    override fun onInterrupt() = Unit
}
