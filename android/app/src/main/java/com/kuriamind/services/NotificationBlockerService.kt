package com.kuriamind.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.kuriamaindo.repositories.BlockRepository
import com.kuriamind.utils.BlockUtils

class NotificationBlockerService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        BlockRepository.invalidateCache()
        Log.d("NotificationBlocker", "Listener connected, invalidated repository cache.")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            val packageName = it.packageName
            Log.d("DEBUG", "Received notification for package: ${packageName}, key: ${sbn.key}")
            if (shouldBlockNotification(packageName, sbn.key)) {
                cancelNotification(sbn.key)
            }
        }
    }

    private fun shouldBlockNotification(packageName: String, notificationKey: String): Boolean {
        val activeBlocks = BlockRepository.getActiveNotificationBlockingBlocks()

        val shouldBlock = activeBlocks.any { block -> BlockUtils.shouldBlock(block, packageName) }

        if (shouldBlock) {
            Log.d(
                    "DEBUG",
                    "Blocking notification for package: $packageName (key: $notificationKey)"
            )
        }
        return shouldBlock
    }
}
