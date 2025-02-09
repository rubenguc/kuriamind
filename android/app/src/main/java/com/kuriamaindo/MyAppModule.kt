package com.kuriamaindo

import android.content.Context
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.kuriamaindo.InstalledAppsHelper
import com.kuriamaindo.SharedPreferencesHelper
import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityManager
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactContextBaseJavaModule

class MyAppModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private val installedAppsHelper = InstalledAppsHelper(reactContext)
    private val sharedPreferencesHelper = SharedPreferencesHelper(reactContext)

    override fun getName(): String {
        return "MyAppModule"
    }

    @ReactMethod
    fun getInstalledApps(promise: Promise) {
        try {
            val appsList = installedAppsHelper.getInstalledApps()
            promise.resolve(appsList)
        } catch (e: Exception) {
            promise.reject("ERROR_GET_APPS", e.message)
        }
    }

    @ReactMethod
    fun getBlockedApps(promise: Promise) {
        try {
            val blockedApps = sharedPreferencesHelper.getBlockedApps()
            promise.resolve(blockedApps)
        } catch (e: Exception) {
            promise.reject("ERROR_GET_BLOCKED_APPS", e.message)
        }
    }

    @ReactMethod
    fun saveBlockedApps(apps: ReadableArray, promise: Promise) {
        try {
            val appsList = mutableListOf<String>()
            for (i in 0 until apps.size()) {
                appsList.add(apps.getString(i))
            }
            sharedPreferencesHelper.saveBlockedApps(appsList)
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject("ERROR_SAVE_BLOCKED_APPS", e.message)
        }
    }

    fun isAccessibilityServiceEnabled(context: Context, service: Class<out AccessibilityService>): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServices)
        
        while (colonSplitter.hasNext()) {
            val componentName = ComponentName.unflattenFromString(colonSplitter.next())
            if (componentName?.className == service.name) {
                return true
            }
        }
        return false
    }

    @ReactMethod
    fun checkAccessibilityServiceStatus(promise: Promise) {
        val isEnabled = isAccessibilityServiceEnabled(reactApplicationContext, AppMonitorService::class.java)
        promise.resolve(isEnabled)
    }

}
