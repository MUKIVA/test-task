package com.github.mukiva.testtask.data

import com.github.mukiva.testtask.data.models.Node
import com.github.mukiva.testtask.data.utils.RequestResult
import com.github.mukiva.testtask.database.NodeDatabase
import kotlinx.coroutines.flow.Flow

interface NodeRepository {
    suspend fun getNode(id: String? = null): Flow<RequestResult<Node>>
    suspend fun addNode(parentId: String)
    suspend fun deleteNode(id: String)
}

fun createRepository(
    database: NodeDatabase
): NodeRepository = NodeRepositoryImpl(database)