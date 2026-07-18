package com.kuriamind.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocks")
data class BlockEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val blockedApps: List<String>,
    val blockApps: Boolean,
    val blockNotifications: Boolean,
    val isActive: Boolean,
    val startTime: String,
    val endTime: String,
)

fun BlockEntity.toDomain(): com.kuriamind.domain.model.Block = com.kuriamind.domain.model.Block(
    id = id,
    name = name,
    blockedApps = blockedApps,
    blockApps = blockApps,
    blockNotifications = blockNotifications,
    isActive = isActive,
    startTime = startTime,
    endTime = endTime,
)

fun com.kuriamind.domain.model.Block.toEntity(): BlockEntity = BlockEntity(
    id = id,
    name = name,
    blockedApps = blockedApps,
    blockApps = blockApps,
    blockNotifications = blockNotifications,
    isActive = isActive,
    startTime = startTime,
    endTime = endTime,
)
