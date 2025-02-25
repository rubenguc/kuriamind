package com.kuriamind.modules.installedApps

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import java.io.ByteArrayOutputStream

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
                    val appMap =
                            WritableNativeMap().apply {
                                putString("packageName", packageName)
                                putString(
                                        "appName",
                                        packageManager.getApplicationLabel(appInfo).toString()
                                )
                                putString("icon", getAppIconAsBase64(packageName))
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

    private fun getAppIconAsBase64(packageName: String): String {
        return try {
            val drawable = reactApplicationContext.packageManager.getApplicationIcon(packageName)
            val bitmap = getBitmapFromDrawable(drawable)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.NO_WRAP)
        } catch (e: Exception) {
            ""
        }
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val bitmap =
                    Bitmap.createBitmap(
                            drawable.intrinsicWidth.takeIf { it > 0 } ?: 100,
                            drawable.intrinsicHeight.takeIf { it > 0 } ?: 100,
                            Bitmap.Config.ARGB_8888
                    )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}
