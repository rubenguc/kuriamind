package com.kuriamind.domain.model

data class Block(
    val id: String,
    val name: String,
    val blockedApps: List<String>, // package names
    val blockApps: Boolean,
    val blockNotifications: Boolean,
    val isActive: Boolean,
    val startTime: String, // "HH:mm" or empty
    val endTime: String,   // "HH:mm" or empty
)
