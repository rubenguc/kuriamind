package com.kuriamind.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
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
class NotificationBlockerService : NotificationListenerService() {

    @Inject lateinit var repository: BlockRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) return
        val packageName = sbn.packageName
        if (packageName == this.packageName) return

        Log.d(TAG, "Notification posted from: $packageName — ${sbn.notification.tickerText ?: sbn.notification.extras.getString("android.title")}")

        scope.launch {
            val activeBlocks = repository.observeAll().first()
            Log.d(TAG, "Found ${activeBlocks.size} active block(s) in DB")

            val matchingBlock = activeBlocks.firstOrNull { block ->
                val isMatch = block.isActive
                        && block.blockNotifications
                        && packageName in block.blockedApps
                        && isTimeInRange(block.startTime, block.endTime)
                Log.d(TAG, "  Check block '${block.name}': isActive=${block.isActive}, blockNotifs=${block.blockNotifications}, inList=${packageName in block.blockedApps}, inTimeRange=${isTimeInRange(block.startTime, block.endTime)} -> ${if (isMatch) "MATCH" else "skip"}")
                isMatch
            }

            if (matchingBlock != null) {
                Log.d(TAG, "CANCELLING notification from $packageName (matched block: '${matchingBlock.name}')")
                cancelNotification(sbn.key)
            } else {
                Log.d(TAG, "No block matches $packageName notification, allowing")
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        // no-op
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private companion object {
        private const val TAG = "NotificationBlockerService"
    }
}
