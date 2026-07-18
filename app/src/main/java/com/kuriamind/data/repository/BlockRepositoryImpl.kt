package com.kuriamind.data.repository

import android.util.Log
import com.kuriamind.data.local.BlockDao
import com.kuriamind.data.local.toDomain
import com.kuriamind.data.local.toEntity
import com.kuriamind.domain.model.Block
import com.kuriamind.domain.repository.BlockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "BlockRepository"

@Singleton
class BlockRepositoryImpl @Inject constructor(
    private val dao: BlockDao,
) : BlockRepository {

    override fun observeAll(): Flow<List<Block>> {
        Log.d(TAG, "observeAll() called")
        return dao.observeAll().map { entities ->
            val blocks = entities.map { it.toDomain() }
            Log.d(TAG, "observeAll() emitted ${blocks.size} blocks")
            blocks
        }
    }

    override suspend fun getById(id: String): Block? {
        Log.d(TAG, "getById($id)")
        return dao.getById(id)?.toDomain()
    }

    override suspend fun save(block: Block) {
        Log.d(TAG, "save() block.id=${block.id}, name=${block.name}")
        dao.upsert(block.toEntity())
        Log.d(TAG, "save() completed")
    }

    override suspend fun update(block: Block) {
        Log.d(TAG, "update() block.id=${block.id}")
        dao.update(block.toEntity())
    }

    override suspend fun deleteById(id: String) {
        Log.d(TAG, "deleteById($id)")
        dao.deleteById(id)
    }

    override suspend fun toggleActive(id: String) {
        Log.d(TAG, "toggleActive($id)")
        dao.toggleActive(id)
    }
}
