package com.kuriamind.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.TextView
import com.facebook.react.BuildConfig
import com.kuriamind.MainActivity
import com.kuriamind.R
import com.kuriamind.modules.blocks.Block
import java.time.LocalTime

class AppMonitorService : AccessibilityService() {

    private val CHANNEL_ID = "AppMonitorServiceChannel"
    private val NOTIFICATION_ID = 1
    private var blockPopupView: View? = null
    private var isPopupActive: Boolean = false
    private var isRedirectingToHome: Boolean = false

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
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (it.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                val packageName = it.packageName?.toString() ?: return

                if (isRedirectingToHome) return

                val activeBlocks = getActiveBlocks()

                activeBlocks.forEach { b ->
                    if (BuildConfig.DEBUG) {
                        Log.d(
                                "DEBUG",
                                "package name: $packageName, isRedirectingToHome: $isRedirectingToHome"
                        )
                    }

                    var shouldBlock =
                            isPackageNameInList(packageName, b.blockedApps) &&
                                    isTimeWithinWindow(b, getCurrentTimeInMinutes())

                    if (shouldBlock) {
                        showBlockPopup(packageName)
                        return@forEach
                    }
                }
            }
        }
    }

    private fun getActiveBlocks(): List<Block> {
        val blockStorage = BlockLocator.provideBlockStorage(applicationContext)
        return blockStorage.getItems().filter { block -> block.isActive && block.blockApps }
    }

    private fun isPackageNameInList(packageName: String, list: List<String>): Boolean {
        return list.contains(packageName)
    }

    private fun showBlockPopup(packageName: String) {
        if (isPopupActive) return

        try {

            isPopupActive = true

            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            blockPopupView = inflater.inflate(R.layout.popup_block_screen, null)

            val blockMessageTextView: TextView? = blockPopupView?.findViewById(R.id.block_message)

            val blockedMessage =
                    SettingsLocator.provideSettingsStorage(applicationContext)
                            .getValue("blockMessage", "App Blocked")
            blockMessageTextView?.text = blockedMessage

            val layoutParams =
                    WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                            PixelFormat.TRANSLUCENT
                    )

            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.addView(blockPopupView, layoutParams)

            val closeButton = blockPopupView?.findViewById<Button>(R.id.close_button)
            closeButton?.setOnClickListener {
                isRedirectingToHome = true
                redirectToHome()
                removeBlockPopup()
            }
        } catch (e: Exception) {
            Log.e("DEBUG", "Error showing block popup", e)
        }
    }

    private fun forceStopApp(packageName: String) {
        try {
            val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.killBackgroundProcesses(packageName)
            val process = Runtime.getRuntime().exec("am force-stop $packageName")
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeBlockPopup() {
        blockPopupView?.let {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.removeView(it)
            blockPopupView = null
            isPopupActive = false
        }
    }

    private fun redirectToHome() {
        val homeIntent =
                Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
        startActivity(homeIntent)
        Handler(Looper.getMainLooper()).postDelayed({ isRedirectingToHome = false }, 1000)
    }

    override fun onInterrupt() {}
    private fun createNotificationChannel() {
        val serviceChannel =
                NotificationChannel(
                                CHANNEL_ID,
                                "App Monitor Service Channel",
                                NotificationManager.IMPORTANCE_DEFAULT
                        )
                        .apply { description = "Channel for application monitoring service" }

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
                            .setContentTitle("Monitoring")
                            .setSmallIcon(R.drawable.ic_icon)
                            .setContentIntent(pendingIntent)
                            .build()

            startForeground(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("DEBUG", e.toString())
            }
        }
    }

    fun isTimeWithinWindow(block: Block, currentTimeMinutes: Long): Boolean {
        if (block.startTime.isEmpty() && block.endTime.isEmpty()) {
            return true
        }

        val blockStartTime = parseTimeStringToMinutes(block.startTime)
        val blockEndTime = parseTimeStringToMinutes(block.endTime)

        val adjustedCurrentTime =
                if (blockStartTime > blockEndTime) {
                    currentTimeMinutes + 1440L // 24 hours in minutes
                } else {
                    currentTimeMinutes
                }

        if (BuildConfig.DEBUG) {
            Log.d("DEBUG", "blockStartTime: $blockStartTime")
            Log.d("DEBUG", "adjustedCurrentTime: $adjustedCurrentTime")
            Log.d("DEBUG", "blockEndTime: $blockEndTime")
        }

        return adjustedCurrentTime >= blockStartTime && adjustedCurrentTime <= blockEndTime
    }

    fun getCurrentTimeInMinutes(): Long {
        val currentDateTime = LocalTime.now()
        return (currentDateTime.hour * 60 + currentDateTime.minute).toLong()
    }

    private fun parseTimeStringToMinutes(timeString: String): Long {
        try {
            val parts = timeString.split(":")
            val hours = parts[0].toInt()
            val minutes = parts[1].toInt()
            return hours * 60L + minutes
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("DEBUG", e.toString())
            }
            return 0L
        }
    }
}
