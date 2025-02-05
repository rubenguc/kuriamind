package com.kuriamaindo

import android.content.Context
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.kuriamaindo.InstalledAppsHelper
import com.kuriamaindo.SharedPreferencesHelper

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
}
