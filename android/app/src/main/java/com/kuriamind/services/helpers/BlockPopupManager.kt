package com.kuriamind.services.helpers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.kuriamind.R
import com.kuriamind.services.StatsLocator
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BlockPopupManager(private val context: Context) {

    private var blockPopupView: View? = null
    private var isPopupActive: Boolean = false
    private var isRedirectingToHome: Boolean = false

    private val settingsSharedPref: SharedPreferences =
            context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val TAG = "BlockPopupManager"
    }

    fun showBlockPopup(packageName: String) {
        if (isPopupActive) return

        try {
            isPopupActive = true
            blockPopupView = layoutInflater.inflate(R.layout.popup_block_screen, null)

            // --- Find Views ---
            val appIconImageView: ImageView? = blockPopupView?.findViewById(R.id.app_icon)
            val appNameTextView: TextView? = blockPopupView?.findViewById(R.id.app_name)
            val blockMessageTextView: TextView? = blockPopupView?.findViewById(R.id.block_message)
            val blockCountTextView: TextView? = blockPopupView?.findViewById(R.id.block_count_text)
            val closeButton: Button? =
                    blockPopupView?.findViewById(R.id.close_button) // Changed to allow null

            // --- Populate Views ---
            populateAppInfo(packageName, appIconImageView, appNameTextView)
            populateBlockMessage(blockMessageTextView)
            populateBlockCount(packageName, blockCountTextView)

            // --- Setup Popup Window ---
            val layoutParams = createLayoutParams()
            windowManager.addView(blockPopupView, layoutParams)

            // --- Setup Close Button ---
            closeButton?.setOnClickListener {
                isRedirectingToHome = true // Set flag before starting redirect
                redirectToHome()
            }
            Log.d(TAG, "Block popup shown for $packageName")
        } catch (e: Exception) {
            Log.e(TAG, "Error showing block popup for $packageName", e)
            cleanUpPopupResources() // Ensure cleanup on error
        }
    }

    private fun populateAppInfo(packageName: String, iconView: ImageView?, nameView: TextView?) {
        var appName = packageName
        try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            appName = pm.getApplicationLabel(appInfo).toString()
            val appIcon = pm.getApplicationIcon(appInfo)
            iconView?.setImageDrawable(appIcon)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.w(TAG, "Could not find package info for $packageName", e)
            iconView?.setImageResource(R.mipmap.ic_launcher) // Set default/placeholder
        } catch (e: Exception) {
            Log.e(TAG, "Error getting app info for $packageName", e)
            iconView?.setImageResource(R.mipmap.ic_launcher) // Set default/placeholder
        }
        nameView?.text = appName
    }

    private fun populateBlockMessage(messageView: TextView?) {
        val defaultMessage = context.getString(R.string.popup_default_block_message)
        val blockedMessage = settingsSharedPref.getString("blockMessage", null) ?: defaultMessage
        messageView?.text = blockedMessage
    }

    private fun populateBlockCount(packageName: String, countView: TextView?) {
        var blockCountToday = 0
        try {
            val today = LocalDate.now()
            val statsList = StatsLocator.provideStatsStorage(context).getStats(today, today)
            val todayStats =
                    statsList.firstOrNull {
                        it.date == today.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    }
            blockCountToday = todayStats?.appStats?.get(packageName)?.appBlockCount ?: 0
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching stats for $packageName", e)
        }
        countView?.text = context.getString(R.string.popup_block_count_text, blockCountToday)
    }

    private fun createLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams
                        .FLAG_LAYOUT_IN_SCREEN, // Adjust flags if interaction needed
                PixelFormat.TRANSLUCENT
        )
    }

    fun removeBlockPopup() {
        // Ensure removal happens on the main thread
        handler.post { cleanUpPopupResources() }
    }

    // This method must run on the main thread
    private fun cleanUpPopupResources() {
        if (blockPopupView != null) {
            Log.d(TAG, "Removing block popup view")
            try {
                windowManager.removeViewImmediate(
                        blockPopupView
                ) // Use removeViewImmediate if possible inside handler
            } catch (e: IllegalArgumentException) {
                Log.w(TAG, "Error removing block popup view (already removed?): ${e.message}")
            } catch (e: Exception) {
                Log.e(TAG, "Error removing block popup view", e)
            } finally {
                blockPopupView = null // Ensure view reference is cleared
                isPopupActive = false // Reset flag after view is gone or error occurred
            }
        } else {
            // If view was already null, ensure flag is false
            isPopupActive = false
        }
        // Reset redirect flag whenever popup is removed/cleaned up, unless mid-redirect
        if (!isRedirectingToHome) {
            // If not currently in the process of redirecting, reset the flag.
            // The flag is reset inside redirectToHome after the delay otherwise.
        }
    }

    private fun redirectToHome() {
        val homeIntent =
                Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    // FLAG_ACTIVITY_NEW_TASK is required when starting from a service context
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
        try {
            Log.d(TAG, "Redirecting to home screen")
            context.startActivity(homeIntent)
            // Delay removal slightly to allow the home screen to appear
            handler.postDelayed(
                    {
                        removeBlockPopup() // Remove popup after delay
                        isRedirectingToHome = false // Reset flag after redirect action is completed
                        Log.d(TAG, "Finished redirecting, popup removed, flag reset.")
                    },
                    500
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error redirecting to home", e)
            // Clean up immediately if redirect fails
            removeBlockPopup()
            isRedirectingToHome = false // Reset flag on error too
        }
    }

    // Public getter for the state if AppMonitorService needs it
    fun isPopupActive(): Boolean = isPopupActive
    fun isRedirectingToHome(): Boolean = isRedirectingToHome
}
