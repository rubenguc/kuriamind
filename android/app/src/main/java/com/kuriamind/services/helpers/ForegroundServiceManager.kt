package com.kuriamind.services.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import com.kuriamind.MainActivity
import com.kuriamind.R
import com.kuriamind.repositories.BlockRepository

class ForegroundServiceManager(private val service: Service) {

    private val context: Context = service.applicationContext
    private var isForeground: Boolean = false

    companion object {
        private const val CHANNEL_ID = "AppMonitorServiceChannel" // Keep consistent ID
        private const val NOTIFICATION_ID = 1
        private const val TAG = "ForegroundServiceMgr"
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = context.getString(R.string.app_monitor_channel_name)
            val channelDesc = context.getString(R.string.app_monitor_channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW // Use low to minimize intrusion
            val channel =
                    NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                        description = channelDesc
                        // Configure other channel settings if needed (e.g., sound, vibration off)
                        setSound(null, null)
                        enableVibration(false)
                    }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
            Log.d(TAG, "Notification channel created or verified.")
        }
    }

    fun checkAndUpdateServiceState() {
        try {
            val activeBlocks = BlockRepository.getActiveAppBlockingBlocks()
            val shouldBeForeground = activeBlocks.isNotEmpty()
            Log.d(
                    TAG,
                    "Check State: shouldBeForeground=$shouldBeForeground, isForeground=$isForeground"
            )

            if (shouldBeForeground && !isForeground) {
                startForeground()
            } else if (!shouldBeForeground && isForeground) {
                stopForeground()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking and updating service state", e)
        }
    }

    private fun startForeground() {
        if (isForeground) return

        try {
            val notification = createNotification()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                service.startForeground(
                        NOTIFICATION_ID,
                        notification,
                        // Use the appropriate type based on Android version and manifest
                        // declaration
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            } else {
                service.startForeground(NOTIFICATION_ID, notification)
            }
            isForeground = true
            Log.d(TAG, "Service started in foreground.")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground service", e)
            isForeground = false // Ensure state is correct on error
        }
    }

    private fun stopForeground() {
        if (!isForeground) return
        try {
            service.stopForeground(Service.STOP_FOREGROUND_REMOVE)
            isForeground = false
            Log.d(TAG, "Service stopped foreground state.")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping foreground service", e)
            isForeground = false // Ensure state is correct on error
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent =
                Intent(context, MainActivity::class.java) // Intent to open when tapped
        val pendingIntentFlags =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
        val pendingIntent =
                PendingIntent.getActivity(context, 0, notificationIntent, pendingIntentFlags)

        val notificationTitle = context.getString(R.string.app_monitor_notification_title)
        val notificationText = context.getString(R.string.app_monitor_notification_text)

        // Ensure channel is created before building notification for API >= 26
        createNotificationChannel()

        return Notification.Builder(context, CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.ic_icon) // Ensure this drawable exists
                .setContentIntent(pendingIntent)
                .setOngoing(true) // Makes it non-dismissible
                .build()
    }

    // Public getter if needed elsewhere
    fun isForeground(): Boolean = isForeground
}
