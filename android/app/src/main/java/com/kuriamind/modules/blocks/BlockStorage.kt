package com.kuriamind.modules.blocks

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.kuriamind.core.StorageManager

class BlockStorage(context: Context) :
        StorageManager<Block>(
                context,
                "blocks_storage",
                "blocks_data",
                object : TypeToken<List<Block>>() {}
        )
