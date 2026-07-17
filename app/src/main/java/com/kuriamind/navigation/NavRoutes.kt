package com.kuriamind.navigation

import kotlinx.serialization.Serializable

@Serializable
object Welcome

@Serializable
object Main

@Serializable
data class Block(val blockId: String = "")
