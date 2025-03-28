package com.kuriamind.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.kuriamind.modules.blocks.Block

class NotificationBlockerService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            val packageName = it.packageName

            if (shouldBlockNotification(packageName)) {
                cancelNotification(sbn.key)
            }
        }
    }

    private fun shouldBlockNotification(packageName: String): Boolean {
        val activeBlocks = getActiveBlocks()
        return activeBlocks.any { block -> isPackageNameInList(packageName, block.blockedApps) }
    }

    private fun getActiveBlocks(): List<Block> {
        val blockStorage = BlockLocator.provideBlockStorage(applicationContext)
        return blockStorage.getItems().filter { block ->
            block.isActive && block.blockNotifications
        }
    }

    private fun isPackageNameInList(packageName: String, list: List<String>): Boolean {
        return list.contains(packageName)
    }
}
