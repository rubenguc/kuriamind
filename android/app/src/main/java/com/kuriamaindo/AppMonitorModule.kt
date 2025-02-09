package com.kuriamaindo

import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.NativeModule

class AppMonitorModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "AppMonitorModule"

    @ReactMethod
    fun startMonitoring() {
        Log.d("DEBUG", "startMonitoring")
        val intent = Intent(reactApplicationContext, AppMonitorService::class.java)
        reactApplicationContext.startService(intent)
    }

    @ReactMethod
    fun stopMonitoring() {
        Log.d("DEBUG", "stopMonitoring")
        val intent = Intent(reactApplicationContext, AppMonitorService::class.java)
        reactApplicationContext.stopService(intent)
    }
}
