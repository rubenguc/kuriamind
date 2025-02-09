package com.kuriamaindo

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.content.Intent
import android.content.Context
import android.util.Log

class AppMonitorService : AccessibilityService() {
    
    // companion object {
    //     var blockedApps = listOf<String>() // Lista de apps bloqueadas
    // }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val sharedPreferences = getSharedPreferences("BlockedAppsPrefs", Context.MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("monitoring_enabled", false)) {
            Log.d("DEBUG", "Disabled")
            return // Salir si el monitoreo no está habilitado
        }

        Log.d("DEBUG", "onAccessibilityEvent")
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            Log.d("DEBUG", "App detectada: $packageName")

            val blockedApps = sharedPreferences.getStringSet("blockedApps", emptySet()) ?: emptySet()

            Log.d("DEBUG", "BlockedApps: $blockedApps")

            if (blockedApps.contains(packageName)) {
                Log.d("AppMonitorService", "App bloqueada detectada: $packageName")
                showBlockScreen(packageName)
            }
        }
    }

    override fun onInterrupt() {
        Log.d("DEBUG", "Servicio interrumpido")
    }

    private fun showBlockScreen(packageName: String) {
        Log.d("DEBUG", "showBlockScreen")
        val intent = Intent(this, BlockActivity::class.java).apply {
            putExtra("BLOCKED_PACKAGE", packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK // Necesario para lanzar actividad desde un servicio
        }
        startActivity(intent)
    }
}
