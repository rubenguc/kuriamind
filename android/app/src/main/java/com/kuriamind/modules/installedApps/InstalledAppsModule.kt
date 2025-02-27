package com.kuriamind.modules.installedApps

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import java.io.File
import java.io.FileOutputStream

class InstalledAppsModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "InstalledAppsModule"
    }

    @ReactMethod
    fun getAll(promise: Promise) {
        try {
            val packageManager = reactApplicationContext.packageManager
            val appsList = mutableListOf<WritableMap>()
            val addedPackages = mutableSetOf<String>()

            val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_LAUNCHER) }

            val apps = packageManager.queryIntentActivities(intent, 0)

            apps.forEach { resolveInfo ->
                val appInfo = resolveInfo.activityInfo.applicationInfo
                val packageName = appInfo.packageName

                val isSamePackage = packageName == reactApplicationContext.packageName
                val appIsAdded = addedPackages.contains(packageName)

                if (!isSamePackage && !appIsAdded) {
                    val appMap = WritableNativeMap().apply {
                        putString("packageName", packageName)
                        putString(
                            "appName",
                            packageManager.getApplicationLabel(appInfo).toString()
                        )
                        putString("icon", getAppIconUri(packageName))
                    }

                    appsList.add(appMap)
                    addedPackages.add(packageName)
                }
            }

            appsList.sortBy { it.getString("appName") }

            val sortedAppsList = WritableNativeArray()
            appsList.forEach { sortedAppsList.pushMap(it) }

            promise.resolve(sortedAppsList)
        } catch (e: Exception) {
            promise.reject(e)
        }
    }

    private fun getAppIconUri(packageName: String): String {
        return try {
            val cacheFile = File(reactApplicationContext.cacheDir, "${packageName}_icon.png")
            
            if (!cacheFile.exists()) {
                val drawable = reactApplicationContext.packageManager.getApplicationIcon(packageName)
                val originalBitmap = getBitmapFromDrawable(drawable)
                
                val scaledBitmap = Bitmap.createScaledBitmap(
                    originalBitmap,
                    128,  
                    128, 
                    true
                )
                
                FileOutputStream(cacheFile).use { stream ->
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 85, stream)
                    stream.flush()
                }
                
                originalBitmap.recycle()
                scaledBitmap.recycle()
            }
            
            "file://${cacheFile.absolutePath}"
        } catch (e: Exception) {
            ""
        }
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val width = drawable.intrinsicWidth.coerceAtLeast(1)
            val height = drawable.intrinsicHeight.coerceAtLeast(1)
            
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            
            bitmap
        }
    }

    @ReactMethod
    fun clearIconCache(promise: Promise) {
        try {
            reactApplicationContext.cacheDir.listFiles()?.forEach { file ->
                if (file.name.endsWith("_icon.png")) {
                    file.delete()
                }
            }
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject(e)
        }
    }
}