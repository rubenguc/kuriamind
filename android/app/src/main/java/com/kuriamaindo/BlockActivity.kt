package com.kuriamaindo

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class BlockActivity : AppCompatActivity() {

    private var blockedPackage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG", "onCreate")
        setContentView(R.layout.activity_block)

        // Obtener el paquete de la app bloqueada
        blockedPackage = intent.getStringExtra("BLOCKED_PACKAGE")

        Log.d("DEBUG", "blockedPackage: $blockedPackage")


        val closeButton = findViewById<Button>(R.id.close_button)
        closeButton.setOnClickListener {
            blockedPackage?.let { packageName ->
                forceStopApp(packageName)
            }
        
            // Enviar al usuario al Home para evitar que la app se reabra automáticamente
            val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(homeIntent)
        
            finish() // Cerrar la pantalla de bloqueo
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


// import com.kuriamaindo.R
// import android.app.Activity
// import android.os.Bundle
// import android.os.Handler
// import android.os.Looper
// import android.widget.Button
// import android.widget.TextView
// import android.util.Log

// class BlockActivity : Activity() {

//     override fun onCreate(savedInstanceState: Bundle?) {
//         super.onCreate(savedInstanceState)
//         Log.d("DEBUG", "onCreate")
//         setContentView(R.layout.activity_block)

//         // Cierra la app bloqueada
//         Handler(Looper.getMainLooper()).postDelayed({
//             finishAffinity() // Cierra todas las actividades de la app bloqueada
//         }, 3000) // Cierra después de 3 segundos
//     }

//     override fun onBackPressed() {

//     }
// }
