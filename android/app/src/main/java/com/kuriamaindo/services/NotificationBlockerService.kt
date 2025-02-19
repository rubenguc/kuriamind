package com.kuriamaindo.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.kuriamaindo.modules.blocks.Block

class NotificationBlockerService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("DEBUG", "Servicio de bloqueo de notificaciones conectado")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.let {
            val packageName = it.packageName
            Log.d("DEBUG", "Interceptando notificación de: $packageName")

            if (shouldBlockNotification(packageName)) {
                cancelNotification(sbn.key)
                Log.d("DEBUG", "Notificación bloqueada: $packageName")
            }
        }
    }

    private fun shouldBlockNotification(packageName: String): Boolean {
        val activeBlocks = getActiveBlocks()
        return activeBlocks.any { block -> isPackageNameInList(packageName, block.blockedApps) }
    }

    private fun getActiveBlocks(): List<Block> {
        val blockStorage = ServiceLocator.provideBlockStorage(applicationContext)
        return blockStorage.getItems().filter { block ->
            block.isActive && block.blockNotifications
        }
    }

    private fun isPackageNameInList(packageName: String, list: List<String>): Boolean {
        return list.contains(packageName)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        sbn?.let { Log.d("DEBUG", "Notificación eliminada: ${it.packageName}") }
    }
}
