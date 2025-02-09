package com.kuriamaindo

import android.content.Context
import android.content.SharedPreferences
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise

class MonitorModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private val prefs: SharedPreferences =
        reactContext.getSharedPreferences("BlockedAppsPrefs", Context.MODE_PRIVATE)

    override fun getName(): String {
        return "MonitorModule"
    }

    @ReactMethod
    fun enableMonitoring(promise: Promise) {
        prefs.edit().putBoolean("monitoring_enabled", true).apply()
        promise.resolve(true)
    }

    @ReactMethod
    fun disableMonitoring(promise: Promise) {
        prefs.edit().putBoolean("monitoring_enabled", false).apply()
        promise.resolve(true)
    }

    @ReactMethod
    fun isMonitoringEnabled(promise: Promise) {
        val isEnabled = prefs.getBoolean("monitoring_enabled", false)
        promise.resolve(isEnabled)
    }
}
