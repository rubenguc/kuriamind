package com.kuriamaindo.modules.blocks

import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.google.gson.Gson

class BlockModule(reactContext: ReactApplicationContext) :
        ReactContextBaseJavaModule(reactContext) {

    private val storage = BlockStorage(reactContext)

    override fun getName(): String {
        return "BlockModule"
    }

    @ReactMethod
    fun getAllBlocks(promise: Promise) {
        try {
            var blocks = storage.getItems()

            val json = Gson().toJson(blocks)

            promise.resolve(json)
        } catch (e: Exception) {
            Log.d("DEBUG", "error getAllBlocks: $e")
            promise.reject("ERROR_GET_BLOCK", "Failed to get block data", e)
        }
    }

    @ReactMethod
    fun saveBlock(blockJson: String, promise: Promise) {
        try {
            val gson = Gson()
            val data = gson.fromJson(blockJson, Block::class.java)

            Log.d("DEBUG", "saving block: $data")

            var block =
                    Block(
                            data.name,
                            data.blockedApps,
                            data.blockApps,
                            data.blockNotifications,
                            true
                    )

            storage.addItem(block)

            promise.resolve("Block ${block.name} saved successfully")
        } catch (e: Exception) {
            Log.d("DEBUG", "error saveBlock: $e")
            promise.reject("ERROR_SAVE_BLOCK", "Failed to save block data", e)
        }
    }

    @ReactMethod
    fun updateBlock(updatedBlockJson: String, promise: Promise) {
        try {
            val gson = Gson()
            val updatedBlock = gson.fromJson(updatedBlockJson, Block::class.java)

            Log.d("DEBUG", "updateBlock: $updatedBlockJson")

            val blockId = updatedBlock.id

            // Check if the block with the given name exists
            val existingBlock = storage.getItems().find { it.name == updatedBlock.name }
            if (existingBlock != null) {
                promise.reject(
                        "ERROR_UPDATE_BLOCK",
                        "Block with name $updatedBlock.name already exists"
                )
                return
            }

            storage.updateItem(updatedBlock, { block -> block.id == blockId })

            promise.resolve("Block ${updatedBlock.name} updated successfully")
        } catch (e: Exception) {
            Log.d("DEBUG", "error updateBlock: $e")
            promise.reject("ERROR_UPDATE_BLOCK", "Failed to update block data", e)
        }
    }

    @ReactMethod
    fun deleteBlock(blockId: String, promise: Promise) {
        try {
            Log.d("DEBUG", "deleteBlock: $blockId")
            storage.deleteItem({ block -> block.id == blockId })

            promise.resolve("Block with ID $blockId deleted successfully")
        } catch (e: Exception) {
            Log.d("DEBUG", "error deleteBlock: $e")
            promise.reject("ERROR_DELETE_BLOCK", "Failed to delete block data", e)
        }
    }

    fun getAllActiveBlocks(): List<Block> {
        try {
            var blocks = storage.getItems()
            val activeBlocks = blocks.filter { it.isActive }

            return activeBlocks
        } catch (e: Exception) {
            Log.d("DEBUG", "error getAllActiveBlocks: $e")
            return emptyList()
        }
    }
}
