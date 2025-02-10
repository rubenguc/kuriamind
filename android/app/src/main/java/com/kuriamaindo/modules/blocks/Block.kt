package com.kuriamaindo.modules.blocks

import com.kuriamaindo.core.HasIdBase

data class Block(
        val name: String,
        val blockedApps: List<String>,
        val blockApps: Boolean,
        val blockNotifications: Boolean,
        val isActive: Boolean
) : HasIdBase()
