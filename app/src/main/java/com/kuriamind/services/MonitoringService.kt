package com.kuriamind.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kuriamind.MainActivity
import com.kuriamind.R
import com.kuriamind.domain.repository.BlockRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringService : android.app.Service() {

    @Inject lateinit var repository: BlockRepository
    @Inject lateinit var blockedEventCounter: BlockedEventCounter

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var isForeground = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        createNotificationChannel()
        startObserving()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: startId=$startId")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startObserving() {
        scope.launch {
            val activeCountFlow = repository.observeAll()
                .map { blocks -> blocks.count { it.isActive } }
                .distinctUntilChanged()

            var wasActive = false

            combine(
                activeCountFlow,
                blockedEventCounter.count,
            ) { activeCount, blockedCount ->
                activeCount to blockedCount
            }.collect { (activeCount, blockedCount) ->
                if (activeCount > 0 && !wasActive) {
                    blockedEventCounter.reset()
                    Log.d(TAG, "Monitoring started: $activeCount active block(s)")
                }

                wasActive = activeCount > 0

                if (activeCount > 0) {
                    Log.d(TAG, "Updating notification: active=$activeCount, blocked=$blockedCount")
                    val notification = buildNotification(activeCount, blockedCount.coerceAtLeast(0))
                    if (!isForeground) {
                        try {
                            startForeground(NOTIFICATION_ID, notification)
                            isForeground = true
                        } catch (e: SecurityException) {
                            Log.e(TAG, "Missing notification permission", e)
                        }
                    } else {
                        // Already foreground → just update the notification
                        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                        manager.notify(NOTIFICATION_ID, notification)
                    }
                } else if (isForeground) {
                    Log.d(TAG, "No active blocks, removing foreground notification")
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    isForeground = false
                }
            }
        }
    }

    private fun buildNotification(activeCount: Int, blockedCount: Int): Notification {
        val openIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_icon)
            .setContentTitle(getString(R.string.notification_monitoring_title))
            .setContentText(
                getString(R.string.notification_monitoring_content, activeCount, blockedCount)
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        getString(R.string.notification_monitoring_big_text, activeCount, blockedCount)
                    )
            )
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Monitoring",
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = "Shows active monitoring status"
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
    }

    companion object {
        private const val TAG = "MonitoringService"
        private const val CHANNEL_ID = "monitoring"
        private const val NOTIFICATION_ID = 1001

        fun start(context: Context) {
            val intent = Intent(context, MonitoringService::class.java)
            context.startService(intent)
        }
    }
}
