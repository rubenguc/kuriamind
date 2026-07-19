package com.kuriamind.ui.feature.blocks.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuriamind.domain.model.Block
import com.kuriamind.ui.feature.blocks.InstalledAppItem
import com.kuriamind.ui.theme.StatusGreen

@Composable
fun BlockItem(
    block: Block,
    installedApps: Map<String, InstalledAppItem>,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleActive: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // Left accent bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .padding(vertical = 12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (block.isActive) StatusGreen else MaterialTheme.colorScheme.outlineVariant),
            )

            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                // Top row: name + status + menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = block.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )

                    Spacer(Modifier.width(8.dp))

                    // Active/Inactive badge
                    Surface(
                        shape = CircleShape,
                        color = if (block.isActive)
                            StatusGreen.copy(alpha = 0.15f)
                        else
                            MaterialTheme.colorScheme.outlineVariant,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (block.isActive) StatusGreen else MaterialTheme.colorScheme.onSurfaceVariant),
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = if (block.isActive) "Active" else "Inactive",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (block.isActive) StatusGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    BlockMoreMenu(
                        isActive = block.isActive,
                        onEdit = onEdit,
                        onDelete = onDelete,
                        onToggleActive = onToggleActive,
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Feature badge row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (block.blockApps) {
                        FeatureChip(text = "Block Apps")
                    }
                    if (block.blockNotifications) {
                        FeatureChip(text = "Block Notifications")
                    }

                    if (block.startTime.isNotEmpty() && block.endTime.isNotEmpty()) {
                        FeatureChip(
                            text = "${block.startTime} - ${block.endTime}",
                            icon = Icons.Filled.Timer,
                        )
                    }
                }

                // Blocked apps with icons
                if (block.blockedApps.isNotEmpty()) {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        block.blockedApps.take(5).forEach { pkg ->
                            val app = installedApps[pkg]
                            if (app != null) {
                                AppIconChip(app = app)
                            }
                        }
                    }
                    if (block.blockedApps.size > 5) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "+${block.blockedApps.size - 5} more",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppIconChip(app: InstalledAppItem) {
    val iconBitmap = remember(app.icon) { app.icon?.toImageBitmap() }

    if (iconBitmap != null) {
        Image(
            bitmap = iconBitmap,
            contentDescription = app.appName,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
        )
    } else {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outlineVariant),
        )
    }
}

@Composable
private fun FeatureChip(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(12.dp),
                )
                Spacer(Modifier.width(4.dp))
            }
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun BlockMoreMenu(
    isActive: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleActive: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "More options",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    if (isActive) "Deactivate" else "Activate",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                )
            },
            onClick = {
                expanded = false
                onToggleActive()
            },
        )
        DropdownMenuItem(
            text = {
                Text("Edit", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
            },
            onClick = {
                expanded = false
                onEdit()
            },
        )
        DropdownMenuItem(
            text = {
                Text("Delete", color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            },
            onClick = {
                expanded = false
                onDelete()
            },
        )
    }
}

private fun Drawable.toImageBitmap(): ImageBitmap {
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
