package com.kuriamaindo.repositories

import android.content.Context
import android.util.Log
import com.kuriamind.modules.blocks.Block
import com.kuriamind.modules.blocks.BlockStorage // Import BlockStorage directly
import kotlinx.coroutines.* // Import coroutines for potential background loading

// Singleton object to manage block data and caching
object BlockRepository {

    private lateinit var blockStorage: BlockStorage // Use lateinit for initialization
    @Volatile private var cachedAllBlocks: List<Block>? = null
    private val cacheLock = Any()

    // --- Initialization ---
    // Must be called once, e.g., in Application#onCreate
    fun initialize(context: Context) {
        // Instantiate BlockStorage directly here
        blockStorage = BlockStorage(context.applicationContext)
        Log.d("BlockRepository", "Initialized")
        // Optional: Pre-load cache in background on initialization
        // CoroutineScope(Dispatchers.IO).launch { loadAndCacheBlocks() }
    }

    // --- Cache Invalidation ---
    fun invalidateCache() {
        synchronized(cacheLock) {
            if (cachedAllBlocks != null) {
                Log.d("BlockRepository", "Invalidating cache.")
                cachedAllBlocks = null
            }
        }
    }

    // --- Getters for Specific Use Cases ---

    fun getActiveAppBlockingBlocks(): List<Block> {
        val allBlocks = getOrLoadBlocks() // Get all blocks (cached or loaded)
        // Filter for app blocking use case
        return allBlocks.filter { it.isActive && it.blockApps }.also {
            Log.d("BlockRepository", "Returning ${it.size} active app blocking blocks.")
        }
    }

    fun getActiveNotificationBlockingBlocks(): List<Block> {
        val allBlocks = getOrLoadBlocks() // Get all blocks (cached or loaded)
        // Filter for notification blocking use case
        return allBlocks.filter { it.isActive && it.blockNotifications }.also {
            Log.d("BlockRepository", "Returning ${it.size} active notification blocking blocks.")
        }
    }

    // --- Core Cache Logic ---

    // Gets all blocks, using cache if available, otherwise loads from storage
    private fun getOrLoadBlocks(): List<Block> {
        // Fast path: Check cache without lock first
        cachedAllBlocks?.let {
            // Log.d("BlockRepository", "Cache hit. Returning ${it.size} blocks.") // Can be noisy
            return it
        }

        // Slow path: Load and cache if cache was null
        return loadAndCacheBlocks()
    }

    // Loads blocks from storage and populates the cache
    private fun loadAndCacheBlocks(): List<Block> {
        synchronized(cacheLock) {
            // Double-check inside lock: another thread might have loaded it already
            cachedAllBlocks?.let {
                // Log.d("BlockRepository", "Cache hit (inside lock). Returning ${it.size} blocks.")
                return it
            }

            Log.d("BlockRepository", "Cache miss. Loading blocks from storage...")
            return try {
                // Read directly from the initialized blockStorage instance
                blockStorage.getItems().also {
                    cachedAllBlocks = it // Store the loaded list in the cache
                    Log.d("BlockRepository", "Loaded and cached ${it.size} blocks.")
                }
            } catch (e: Exception) {
                Log.e("BlockRepository", "Error loading blocks from storage", e)
                // Don't cache the error state, keep cache null so it retries next time
                cachedAllBlocks = null
                emptyList() // Return empty list on error
            }
        }
    }
}
