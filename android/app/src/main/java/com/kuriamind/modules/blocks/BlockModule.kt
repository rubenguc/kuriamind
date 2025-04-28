package com.kuriamind.modules.blocks

import android.content.Intent
import android.util.Log
import com.facebook.react.BuildConfig
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.google.gson.Gson
import com.kuriamaindo.repositories.BlockRepository
import com.kuriamind.services.AppMonitorService

class BlockModule(reactContext: ReactApplicationContext) :
        ReactContextBaseJavaModule(reactContext) {

    private val storage = BlockStorage(reactContext)

    override fun getName(): String {
        return "BlockModule"
    }

    private fun triggerUpdateNotifications() {
        BlockRepository.invalidateCache()

        Log.d("DEBUG", "BlockModule triggering AppMonitorService update check")
        val intent = Intent(reactApplicationContext, AppMonitorService::class.java)
        intent.action = AppMonitorService.ACTION_UPDATE_STATUS
        try {
            reactApplicationContext.startService(intent)
        } catch (e: SecurityException) {
            Log.e("DEBUG", "Error starting AppMonitorService (Security): ${e.message}", e)
        } catch (e: IllegalStateException) {
            Log.e("DEBUG", "Error starting AppMonitorService (IllegalState): ${e.message}", e)
        } catch (e: Exception) {
            Log.e("DEBUG", "Failed to send update intent to AppMonitorService", e)
        }
    }

    @ReactMethod
    fun getAllBlocks(promise: Promise) {
        try {
            var blocks = storage.getItems()
            val json = Gson().toJson(blocks)
            promise.resolve(json)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("DEBUG", "error getAllBlocks: $e")
            }
            promise.reject("ERROR_GET_BLOCK", "Failed to get block data", e)
        }
    }

    @ReactMethod
    fun saveBlock(blockJson: String, promise: Promise) {
        try {
            val gson = Gson()
            val data = gson.fromJson(blockJson, Block::class.java)

            var block =
                    Block(
                            data.name,
                            data.blockedApps,
                            data.blockApps,
                            data.blockNotifications,
                            true,
                            data.startTime,
                            data.endTime
                    )

            val existingBlock = storage.getItems().find { it.name == block.name }
            if (existingBlock != null) {
                promise.reject("ERROR_SAVE_BLOCK", "Block with name ${block.name} already exists")
                return
            }

            storage.addItem(block)
            triggerUpdateNotifications()

            promise.resolve("Block ${block.name} saved successfully")
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("DEBUG", "error saveBlock: $e")
            }
            promise.reject("ERROR_SAVE_BLOCK", "Failed to save block data", e)
        }
    }

    @ReactMethod
    fun updateBlock(updatedBlockJson: String, promise: Promise) {
        try {
            val gson = Gson()
            val updatedBlock = gson.fromJson(updatedBlockJson, Block::class.java)

            val blockId = updatedBlock.id

            val existingBlock =
                    storage.getItems().find { it.name == updatedBlock.name && it.id != blockId }
            if (existingBlock != null) {
                promise.reject(
                        "ERROR_UPDATE_BLOCK",
                        "Block with name $updatedBlock.name already exists"
                )
                return
            }

            storage.updateItem(updatedBlock, { block -> block.id == blockId })

            triggerUpdateNotifications()

            promise.resolve("Block ${updatedBlock.name} updated successfully")
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("DEBUG", "error updateBlock: $e")
            }
            promise.reject("ERROR_UPDATE_BLOCK", "Failed to update block data", e)
        }
    }

    @ReactMethod
    fun changeBlockStatus(blockId: String, promise: Promise) {
        try {
            val block = storage.getItems().find { it.id == blockId }
            if (block == null) {
                promise.reject("ERROR_CHANGE_BLOCK_STATUS", "Block not found")
                return
            }

            val updatedBlock = block.copy(isActive = !block.isActive)
            storage.updateItem(updatedBlock, { b -> b.id == blockId })

            triggerUpdateNotifications()

            promise.resolve("Block ${updatedBlock.name} status changed successfully")
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("DEBUG", "error changeBlockStatus: $e")
            }
            promise.reject("ERROR_CHANGE_BLOCK_STATUS", "Failed to change block status", e)
        }
    }

    @ReactMethod
    fun deleteBlock(blockId: String, promise: Promise) {
        try {
            storage.deleteItem({ block -> block.id == blockId })

            triggerUpdateNotifications()

            promise.resolve("Block with ID $blockId deleted successfully")
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("DEBUG", "error deleteBlock: $e")
            }
            promise.reject("ERROR_DELETE_BLOCK", "Failed to delete block data", e)
        }
    }
}
