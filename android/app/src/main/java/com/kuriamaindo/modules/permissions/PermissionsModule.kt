package com.kuriamaindo.modules.permissions

import android.content.Intent
import android.provider.Settings
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class PermissionsModule(reactContext: ReactApplicationContext) :
        ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "PermissionsModule"
    }

    @ReactMethod
    fun openNotificationListenerSettings() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        reactApplicationContext.startActivity(intent)
    }

    @ReactMethod
    fun checkNotificationListenerServiceEnabled(promise: Promise) {
        try {
            val contentResolver = reactApplicationContext.contentResolver
            val enabledListeners =
                    Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
            val isEnabled = enabledListeners?.contains(reactApplicationContext.packageName) == true
            promise.resolve(isEnabled)
        } catch (e: Exception) {
            promise.reject("ERROR", "No se pudo verificar el permiso de notificación: ${e.message}")
        }
    }

    @ReactMethod
    fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        reactApplicationContext.startActivity(intent)
    }

    @ReactMethod
    fun checkAccessibilityServiceEnabled(promise: Promise) {
        try {
            val contentResolver = reactApplicationContext.contentResolver
            val enabledServices =
                    Settings.Secure.getString(
                            contentResolver,
                            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
                    )
            val isEnabled = enabledServices?.contains(reactApplicationContext.packageName) == true
            promise.resolve(isEnabled)
        } catch (e: Exception) {
            promise.reject(
                    "ERROR",
                    "No se pudo verificar el permiso de accesibilidad: ${e.message}"
            )
        }
    }
}
