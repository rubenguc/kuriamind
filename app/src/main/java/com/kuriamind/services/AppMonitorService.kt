package com.kuriamind.services

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.kuriamind.domain.repository.BlockRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppMonitorService : AccessibilityService() {

    @Inject lateinit var repository: BlockRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
    }

    @Suppress("DEPRECATION")
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val packageName = event.packageName?.toString() ?: return
        if (packageName == this.packageName) return

        Log.d(TAG, "Window changed to: $packageName")

        scope.launch {
            val activeBlocks = repository.observeAll().first()
            Log.d(TAG, "Found ${activeBlocks.size} active block(s) in DB")

            val matchingBlock = activeBlocks.firstOrNull { block ->
                val isMatch = block.isActive
                        && block.blockApps
                        && packageName in block.blockedApps
                        && isTimeInRange(block.startTime, block.endTime)
                Log.d(TAG, "  Check block '${block.name}': isActive=${block.isActive}, blockApps=${block.blockApps}, inList=${packageName in block.blockedApps}, inTimeRange=${isTimeInRange(block.startTime, block.endTime)} -> ${if (isMatch) "MATCH" else "skip"}")
                isMatch
            }

            if (matchingBlock != null) {
                Log.d(TAG, "BLOCKING $packageName (matched block: '${matchingBlock.name}')")
                performGlobalAction(GLOBAL_ACTION_BACK)

                // Show dialog explaining the block
                showBlockedDialog(packageName)
            } else {
                Log.d(TAG, "No block matches $packageName, allowing")
            }
        }
    }

    private fun showBlockedDialog(packageName: String) {
        val intent = Intent(this, BlockedAppDialogActivity::class.java).apply {
            putExtra(BlockedAppDialogActivity.EXTRA_PACKAGE_NAME, packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        Log.d(TAG, "Launching blocked dialog for $packageName")
        startActivity(intent)
    }

    override fun onInterrupt() {
        Log.d(TAG, "Service interrupted")
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private companion object {
        private const val TAG = "AppMonitorService"
    }
}
