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

        scope.launch {
            val activeBlocks = repository.observeAll().first()
            val shouldBlock = activeBlocks.any { block ->
                block.isActive
                        && block.blockNotifications
                        && packageName in block.blockedApps
                        && isTimeInRange(block.startTime, block.endTime)
            }
            if (shouldBlock) {
                Log.d(TAG, "Blocking notification from $packageName")
                cancelNotification(sbn.key)
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
