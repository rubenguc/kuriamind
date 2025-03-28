package com.kuriamind.modules.blocks

import com.kuriamind.core.HasIdBase

data class Block(
        val name: String,
        val blockedApps: List<String>,
        val blockApps: Boolean,
        val blockNotifications: Boolean,
        val isActive: Boolean,
        val startTime: String,
        val endTime: String
) : HasIdBase()
