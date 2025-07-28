package com.kuriamind.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.kuriamind.repositories.BlockRepository
import com.kuriamind.services.helpers.BlockPopupManager
import com.kuriamind.services.helpers.ForegroundServiceManager // Import helper
import com.kuriamind.utils.BlockUtils

class AppMonitorService : AccessibilityService() {

    companion object {
        const val ACTION_UPDATE_STATUS = "com.kuriamind.services.ACTION_UPDATE_STATUS"
        private const val TAG = "AppMonitorService"
    }

    // Use lateinit for managers that need context/service instance
    private lateinit var foregroundManager: ForegroundServiceManager
    private lateinit var popupManager: BlockPopupManager

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        // Initialize managers here as 'this' (Service instance) is available
        foregroundManager = ForegroundServiceManager(this)
        popupManager = BlockPopupManager(this)

        // Create channel immediately (required for foreground notification)
        foregroundManager.createNotificationChannel()
        // Initial check to start foreground if needed
        foregroundManager.checkAndUpdateServiceState()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand, Action: ${intent?.action}")
        if (intent?.action == ACTION_UPDATE_STATUS) {
            // Delegate state check to the manager
            foregroundManager.checkAndUpdateServiceState()
        }
        // START_STICKY is important for AccessibilityServices that need to stay running
        return START_STICKY
    }

    override fun onServiceConnected() {
        super.onServiceConnected() // Call super
        Log.d(TAG, "onServiceConnected")
        BlockRepository.invalidateCache() // Good place to invalidate cache

        // Configure service info (remains here as it's specific to AccessibilityService)
        serviceInfo =
                AccessibilityServiceInfo().apply {
                    eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                    feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
                    // Consider if other flags are needed, e.g., FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                    // flags = AccessibilityServiceInfo.DEFAULT or
                    // AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                    notificationTimeout = 100 // Keep short for responsiveness
                    // packageNames = null // Monitor all apps unless specific ones are needed
                }
        Log.d(TAG, "Accessibility Service Info configured.")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return // Exit if event is null

        // Check if popup is active or redirecting via the manager
        if (popupManager.isPopupActive() || popupManager.isRedirectingToHome()) {
            return
        }

        if (shouldIgnoreEvent(event)) {
            // If popup was shown for our own app or permission controller, remove it
            if (popupManager.isPopupActive()) {
                popupManager.removeBlockPopup()
            }
            return
        }

        // Only process relevant events if the service should be active (is in foreground)
        if (foregroundManager.isForeground() &&
                        event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
                        event.packageName != null
        ) {

            val packageName = event.packageName.toString()
            Log.d(TAG, "Window Changed: $packageName / ${event.className}") // Keep useful log

            checkAndBlockApp(packageName)
        }
    }

    private fun shouldIgnoreEvent(event: AccessibilityEvent): Boolean {
        // Ignore events if package or class name is missing
        if (event.packageName == null || event.className == null) return true

        val packageName = event.packageName.toString()
        val className = event.className.toString()

        // Ignore events from own app or permission controllers
        return packageName == applicationContext.packageName ||
                className.contains("permissioncontroller", ignoreCase = true) ||
                className.contains("packageinstaller", ignoreCase = true)
    }

    private fun checkAndBlockApp(packageName: String) {
        val currentActiveBlocks = BlockRepository.getActiveAppBlockingBlocks()
        if (currentActiveBlocks.isEmpty()) return

        currentActiveBlocks.forEach { block ->
            if (BlockUtils.shouldBlock(block, packageName)) {
                Log.i(
                        TAG,
                        "Blocking app: $packageName based on Block: ${block.name}"
                ) // Use Info level

                // Increment stats
                StatsLocator.provideStatsStorage(applicationContext).incrementAppBlock(packageName)

                // Delegate showing the popup
                popupManager.showBlockPopup(packageName)

                // Once a block matches and popup is shown, stop checking other blocks for this
                // event
                return
            }
        }
    }

    override fun onInterrupt() {
        Log.w(TAG, "onInterrupt received") // Use Warning level as this indicates an issue
        // Clean up resources managed by helpers
        popupManager.removeBlockPopup()
        // Note: Foreground service state might also be affected, but typically system handles this
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        // Ensure popup is removed if service is destroyed unexpectedly
        popupManager.removeBlockPopup()
        // Foreground service stops automatically when service is destroyed
    }
}
