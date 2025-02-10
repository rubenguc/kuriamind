package com.kuriamaindo.modules.blocks

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.kuriamaindo.core.StorageManager
import com.kuriamaindo.modules.blocks.Block

class BlockStorage(context: Context) : StorageManager<Block>(
    context, "blocks", object : TypeToken<List<Block>>() {}
)
