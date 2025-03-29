package com.kuriamind.services

import android.content.Context
import com.kuriamind.modules.blocks.BlockStorage

object BlockLocator {
    private var blockStorage: BlockStorage? = null

    fun provideBlockStorage(context: Context): BlockStorage {
        if (blockStorage == null) {
            blockStorage = BlockStorage(context.applicationContext)
        }
        return blockStorage!!
    }

    fun reset() {
        blockStorage = null
    }
}
