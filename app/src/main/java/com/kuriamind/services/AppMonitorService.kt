package com.kuriamind.services

import android.accessibilityservice.AccessibilityService
import android.os.SystemClock
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.kuriamind.domain.repository.BlockRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class AppMonitorService : AccessibilityService() {

    @Inject lateinit var repository: BlockRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var lastBlockedPackage = ""
    private var lastBlockedAt = 0L

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
    }

    @Suppress("DEPRECATION")
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val packageName = event.packageName?.toString() ?: return

        if (packageName == this.packageName) return
        if (packageName == lastBlockedPackage && SystemClock.uptimeMillis() - lastBlockedAt < CASCADE_GUARD_MS) return

        scope.launch {
            val activeBlocks = withContext(Dispatchers.IO) {
                repository.observeAll().first()
            }

            val matchingBlock = activeBlocks.firstOrNull { block ->
                block.isActive
                        && block.blockApps
                        && packageName in block.blockedApps
                        && isTimeInRange(block.startTime, block.endTime)
            } ?: return@launch

            Log.d(TAG, "BLOCKING $packageName (block: '${matchingBlock.name}')")
            lastBlockedPackage = packageName
            lastBlockedAt = SystemClock.uptimeMillis()

            withContext(Dispatchers.Main) {
                performGlobalAction(GLOBAL_ACTION_HOME)
                Toast
                    .makeText(
                        this@AppMonitorService,
                        "\"${matchingBlock.name}\" blocked this app",
                        Toast.LENGTH_SHORT,
                    )
                    .show()
            }
        }
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
        /** Prevents cascade: ignores the same package within this window. */
        private const val CASCADE_GUARD_MS = 300L
    }
}
