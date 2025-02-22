package com.kuriamind.modules.blocks

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.kuriamind.core.StorageManager
import com.kuriamind.modules.blocks.Block

class BlockStorage(context: Context) : StorageManager<Block>(
    context, "blocks", object : TypeToken<List<Block>>() {}
)
