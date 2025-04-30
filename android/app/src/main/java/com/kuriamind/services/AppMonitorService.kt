package com.kuriamind.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.facebook.react.BuildConfig
import com.kuriamaindo.repositories.BlockRepository
import com.kuriamind.MainActivity
import com.kuriamind.R
import com.kuriamind.modules.blocks.Block
import com.kuriamind.utils.BlockUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AppMonitorService : AccessibilityService() {

    companion object {
        const val ACTION_UPDATE_STATUS = "com.kuriamind.services.ACTION_UPDATE_STATUS"
        private const val CHANNEL_ID = "AppMonitorServiceChannel"
        private const val NOTIFICATION_ID = 1
    }

    private var blockPopupView: View? = null
    private var isPopupActive: Boolean = false
    private var isRedirectingToHome: Boolean = false
    private var isForeground: Boolean = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        checkAndUpdateServiceState()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DEBUG", "AppMonitorService onStartCommand, Action: ${intent?.action}")
        if (intent?.action == ACTION_UPDATE_STATUS) {
            checkAndUpdateServiceState()
        }
        return START_STICKY
    }

    private fun checkAndUpdateServiceState() {
        val activeBlocks = BlockRepository.getActiveAppBlockingBlocks()
        val shouldBeForeground = activeBlocks.isNotEmpty()
        Log.d(
                "DEBUG",
                "AppMonitorService Check State: shouldBeForeground=$shouldBeForeground, isForeground=$isForeground (using Repository)"
        )

        if (shouldBeForeground && !isForeground) {
            startForegroundServiceInternal()
        } else if (!shouldBeForeground && isForeground) {
            stopForegroundServiceInternal()
        }
    }

    override fun onServiceConnected() {
        BlockRepository.invalidateCache()
        Log.d("DEBUG", "AppMonitorService onServiceConnected, invalidated repository cache.")

        val info =
                AccessibilityServiceInfo().apply {
                    eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                    feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
                    flags = AccessibilityServiceInfo.DEFAULT
                    notificationTimeout = 100
                }
        this.serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (isPopupActive || isRedirectingToHome) return

            if (it.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
                            it.packageName != null &&
                            it.className != null
            ) {
                val packageName = it.packageName.toString()
                val className = it.className.toString()

                if (packageName == applicationContext.packageName ||
                                className.contains("permissioncontroller")
                ) {
                    if (isPopupActive) {
                        removeBlockPopup()
                    }
                    return
                }

                if (isForeground) {
                    val currentActiveBlocks = BlockRepository.getActiveAppBlockingBlocks()

                    if (currentActiveBlocks.isEmpty()) return

                    if (BuildConfig.DEBUG) {
                        Log.d("DEBUG", "Window Changed: $packageName / $className")
                    }

                    currentActiveBlocks.forEach { block ->
                        if (BlockUtils.shouldBlock(block, packageName)) {
                            if (BuildConfig.DEBUG) {
                                Log.d("DEBUG", "Blocking app: $packageName, Block: ${block.name}")
                            }
                            StatsLocator.provideStatsStorage(applicationContext)
                                    .incrementAppBlock(packageName)

                            showBlockPopup(packageName, block)
                            return@let
                        }
                    }
                }
            }
        }
    }

    private fun showBlockPopup(packageName: String, block: Block) {
        if (isPopupActive) return

        try {
            isPopupActive = true

            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            blockPopupView = inflater.inflate(R.layout.popup_block_screen, null)

            // --- Find Views (Make sure these IDs match your XML) ---
            val appIconImageView: ImageView? =
                    blockPopupView?.findViewById(
                            R.id.app_icon
                    ) // Add ImageView with this ID to your XML
            val appNameTextView: TextView? =
                    blockPopupView?.findViewById(R.id.app_name) // Add TextView with this ID
            val blockMessageTextView: TextView? =
                    blockPopupView?.findViewById(R.id.block_message) // Existing TextView
            val blockCountTextView: TextView? =
                    blockPopupView?.findViewById(R.id.block_count_text) // Add TextView with this ID
            val closeButton =
                    blockPopupView?.findViewById<Button>(R.id.close_button) // Existing Button

            // --- Fetch App Info ---
            var appName = packageName // Default to package name
            try {
                val pm = applicationContext.packageManager
                val appInfo = pm.getApplicationInfo(packageName, 0)
                appName = pm.getApplicationLabel(appInfo).toString()
                val appIcon = pm.getApplicationIcon(appInfo)
                appIconImageView?.setImageDrawable(appIcon)
            } catch (e: PackageManager.NameNotFoundException) {
                Log.w("DEBUG", "Could not find package info for $packageName", e)
                // Keep default icon or set a placeholder if you have one
            } catch (e: Exception) {
                Log.e("DEBUG", "Error getting app info for $packageName", e)
            }

            // --- Set App Name ---
            appNameTextView?.text = appName

            // --- Set Block Message ---
            val defaultMessage = getString(R.string.popup_default_block_message)
            val blockedMessage =
                    SettingsLocator.provideSettingsStorage(applicationContext)
                            .getValue("blockMessage", defaultMessage)
            blockMessageTextView?.text = blockedMessage

            // --- Fetch and Set Block Count ---
            var blockCountToday = 0
            try {
                val today = LocalDate.now()
                // Use the getStats method which returns a list
                val statsList =
                        StatsLocator.provideStatsStorage(applicationContext).getStats(today, today)
                // Find today's entry (should be the first if list is not empty and sorted)
                val todayStats =
                        statsList.firstOrNull {
                            it.date == today.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        }
                blockCountToday = todayStats?.appStats?.get(packageName)?.appBlockCount ?: 0
            } catch (e: Exception) {
                Log.e("DEBUG", "Error fetching stats for $packageName", e)
            }
            // Format the string using a resource for better localization
            blockCountTextView?.text = getString(R.string.popup_block_count_text, blockCountToday)

            // --- Setup Popup Window ---
            val layoutParams =
                    WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                            // Removed FLAG_NOT_FOCUSABLE and FLAG_NOT_TOUCH_MODAL
                            // to allow interaction if needed, but kept for overlay behaviour.
                            // Adjust flags as needed for your desired interaction model.
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                            PixelFormat.TRANSLUCENT
                    )

            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.addView(blockPopupView, layoutParams)

            // --- Setup Close Button ---
            closeButton?.setOnClickListener {
                isRedirectingToHome = true
                redirectToHome()
            }
        } catch (e: Exception) {
            Log.e("DEBUG", "Error showing block popup", e)
            isPopupActive = false
            removeBlockPopup()
        }
    }

    private fun removeBlockPopup() {
        Handler(Looper.getMainLooper()).post {
            if (blockPopupView != null) {
                Log.d("DEBUG", "Removing block popup")
                try {
                    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    windowManager.removeView(blockPopupView)
                } catch (e: IllegalArgumentException) {
                    // Can happen if view is already removed or activity is finishing
                    Log.w(
                            "DEBUG",
                            "Error removing block popup view (already removed?): ${e.message}"
                    )
                } catch (e: Exception) {
                    Log.e("DEBUG", "Error removing block popup view", e)
                } finally {
                    blockPopupView = null
                    isPopupActive = false
                }
            } else {
                // Ensure flag is false if view was already null
                isPopupActive = false
            }
        }
    }

    private fun redirectToHome() {
        val homeIntent =
                Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
        try {
            Log.d("DEBUG", "Redirecting to home screen")
            startActivity(homeIntent)
            Handler(Looper.getMainLooper())
                    .postDelayed(
                            {
                                removeBlockPopup()
                                isRedirectingToHome = false
                                Log.d("DEBUG", "Finished redirecting, popup removed, flag reset.")
                            },
                            500
                    )
        } catch (e: Exception) {
            Log.e("DEBUG", "Error redirecting to home", e)
            removeBlockPopup()
            isRedirectingToHome = false
        }
    }

    override fun onInterrupt() {
        Log.d("DEBUG", "AppMonitorService onInterrupt")
        removeBlockPopup()
    }

    private fun createNotificationChannel() {

        val channelName = getString(R.string.app_monitor_channel_name)
        val channelDesc = getString(R.string.app_monitor_channel_description)

        val serviceChannel =
                NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW)
                        .apply { description = channelDesc }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
        Log.d("DEBUG", "Notification channel created.")
    }

    private fun startForegroundServiceInternal() {
        if (isForeground) return

        try {
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntentFlags =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    } else {
                        PendingIntent.FLAG_UPDATE_CURRENT
                    }
            val pendingIntent =
                    PendingIntent.getActivity(this, 0, notificationIntent, pendingIntentFlags)

            val notificationTitle = getString(R.string.app_monitor_notification_title)
            val notificationText = getString(R.string.app_monitor_notification_text)

            val notification: Notification =
                    Notification.Builder(this, CHANNEL_ID)
                            .setContentTitle(notificationTitle)
                            .setContentText(notificationText)
                            .setSmallIcon(R.drawable.ic_icon)
                            .setContentIntent(pendingIntent)
                            .setOngoing(true)
                            .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
            isForeground = true
            Log.d("DEBUG", "AppMonitorService started in foreground.")
        } catch (e: Exception) {
            Log.e("DEBUG", "Error starting foreground service", e)
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            isForeground = false
        }
    }

    private fun stopForegroundServiceInternal() {
        if (!isForeground) return
        try {
            stopForeground(STOP_FOREGROUND_REMOVE)
            isForeground = false
            Log.d("DEBUG", "AppMonitorService stopped foreground state.")
        } catch (e: Exception) {
            Log.e("DEBUG", "Error stopping foreground service", e)
            isForeground = false
        }
    }
}
