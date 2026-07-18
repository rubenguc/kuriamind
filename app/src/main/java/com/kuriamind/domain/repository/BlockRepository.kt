package com.kuriamind.domain.repository

import com.kuriamind.domain.model.Block
import kotlinx.coroutines.flow.Flow

interface BlockRepository {
    fun observeAll(): Flow<List<Block>>
    suspend fun getById(id: String): Block?
    suspend fun save(block: Block)
    suspend fun update(block: Block)
    suspend fun deleteById(id: String)
    suspend fun toggleActive(id: String)
}
