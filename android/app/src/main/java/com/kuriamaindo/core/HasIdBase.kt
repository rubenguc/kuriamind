package com.kuriamind.core

import java.util.UUID

abstract class HasIdBase : HasId {
    override var id: String = generateUniqueId()

    companion object {
        fun generateUniqueId(): String {
            return UUID.randomUUID().toString()
        }
    }
}

interface HasId {
    var id: String
}
