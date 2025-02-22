package com.kuriamind.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.kuriamind.MainActivity
import com.kuriamind.activities.BlockScreenActivity
import com.kuriamind.modules.blocks.Block

class AppMonitorService : AccessibilityService() {

    private val CHANNEL_ID = "AppMonitorServiceChannel"
    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForegroundService()
    }

    override fun onServiceConnected() {
        val info =
                AccessibilityServiceInfo().apply {
                    eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                    feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
                    flags = AccessibilityServiceInfo.DEFAULT
                    notificationTimeout = 100
                }
        this.serviceInfo = info
        Log.d("AppMonitorService", "Servicio de monitoreo conectado")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (it.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                val packageName = it.packageName?.toString()
                val className = it.className?.toString()
                Log.d("AppMonitorService", "App abierta: $packageName - $className")

                val activeBlocks = getActiveBlocks()

                if (packageName == null) return

                activeBlocks.forEach { b ->
                    val appIsBlocked = isPackageNameInList(packageName, b.blockedApps)
                    if (appIsBlocked) {
                        // performGlobalAction(GLOBAL_ACTION_BACK)
                        Log.d("AppMonitorService", "App cerrada: $packageName")
                        showBlockScreen(packageName)
                        return@forEach
                    }
                }
            }
        }
    }

    private fun getActiveBlocks(): List<Block> {
        val blockStorage = ServiceLocator.provideBlockStorage(applicationContext)
        return blockStorage.getItems().filter { block -> block.isActive && block.blockApps }
    }

    private fun isPackageNameInList(packageName: String, list: List<String>): Boolean {
        return list.contains(packageName)
    }

    private fun showBlockScreen(packageName: String) {
        Log.d("DEBUG", "showBlockScreen")
        val intent =
                Intent(this, BlockScreenActivity::class.java).apply {
                    putExtra("BLOCKED_PACKAGE", packageName)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
        startActivity(intent)
    }

    override fun onInterrupt() {
        Log.d("AppMonitorService", "Servicio de monitoreo interrumpido")
    }
    private fun createNotificationChannel() {
        Log.d("DEBUG", "Creating notification channel")
        val serviceChannel =
                NotificationChannel(
                                CHANNEL_ID,
                                "App Monitor Service Channel",
                                NotificationManager.IMPORTANCE_DEFAULT
                        )
                        .apply {
                            description = "Canal para el servicio de monitoreo de aplicaciones"
                        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private fun startForegroundService() {
        try {
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            notificationIntent,
                            PendingIntent.FLAG_IMMUTABLE
                    )

            val notification: Notification =
                    Notification.Builder(this, CHANNEL_ID)
                            .setContentTitle("Monitorizando aplicaciones")
                            .setContentText("El servicio está activo")
                            // .setSmallIcon(R.drawable.ic_monitor)
                            .setContentIntent(pendingIntent)
                            .build()

            startForeground(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)

            Log.d("DEBUG", "Foreground service started")
        } catch (e: Exception) {
            Log.d("DEBUG", "Error starting foreground service")
            Log.d("DEBUG", e.toString())
        }
    }
}
