package com.kuriamind.activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kuriamind.R

class BlockScreenActivity : AppCompatActivity() {

    private var blockedPackage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG", "onCreate")

        setContentView(R.layout.activity_block_screen)

        blockedPackage = intent.getStringExtra("BLOCKED_PACKAGE")

        Log.d("DEBUG", "blockedPackage: $blockedPackage")

        val closeButton = findViewById<Button>(R.id.close_button)
        closeButton.setOnClickListener {
            blockedPackage?.let { packageName -> forceStopApp(packageName) }
            val homeIntent =
                    Intent(Intent.ACTION_MAIN).apply {
                        addCategory(Intent.CATEGORY_HOME)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
            startActivity(homeIntent)
            finish()
        }
    }

    private fun forceStopApp(packageName: String) {
        try {
            Log.d("DEBUG", "forceStopApp")
            val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.killBackgroundProcesses(packageName)

            // También intentar cerrar con 'adb shell' si el dispositivo está rooteado
            val process = Runtime.getRuntime().exec("am force-stop $packageName")
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        // Evitar que el usuario cierre la pantalla de bloqueo con "Atrás"
    }
}
