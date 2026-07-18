package com.kuriamind.services

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.util.Calendar

/**
 * Checks whether the current time falls within the range [startTime]–[endTime].
 *
 * Both times are in "HH:mm" format (24h). Supports overnight ranges (start > end).
 * When either value is blank, the block applies at any time.
 */
internal fun isTimeInRange(
    startTime: String,
    endTime: String,
): Boolean {
    if (startTime.isBlank() || endTime.isBlank()) return true

    val startMinutes = parseMinutes(startTime) ?: return true
    val endMinutes = parseMinutes(endTime) ?: return true

    val now = Calendar.getInstance()
    val currentMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)

    return if (startMinutes <= endMinutes) {
        currentMinutes in startMinutes..endMinutes
    } else {
        currentMinutes >= startMinutes || currentMinutes <= endMinutes
    }
}

private fun parseMinutes(time: String): Int? {
    val parts = time.split(":")
    if (parts.size != 2) return null
    val hours = parts[0].toIntOrNull() ?: return null
    val minutes = parts[1].toIntOrNull() ?: return null
    if (hours !in 0..23 || minutes !in 0..59) return null
    return hours * 60 + minutes
}

/** Converts a platform [Drawable] to a Compose [ImageBitmap]. */
internal fun Drawable.toImageBitmap(): ImageBitmap {
    val bitmap = if (this is BitmapDrawable) {
        bitmap
    } else {
        val w = intrinsicWidth.coerceAtLeast(1)
        val h = intrinsicHeight.coerceAtLeast(1)
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bmp)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        bmp
    }
    return bitmap.asImageBitmap()
}
