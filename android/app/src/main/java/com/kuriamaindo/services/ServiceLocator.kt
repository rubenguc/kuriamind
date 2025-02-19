package com.kuriamaindo.services

import android.content.Context
import com.kuriamaindo.modules.blocks.BlockStorage

object ServiceLocator {
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
