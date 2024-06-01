package com.github.mukiva.testtask.data

import android.util.Log
import com.github.mukiva.testtask.data.models.Node
import com.github.mukiva.testtask.data.utils.HashGenerator
import com.github.mukiva.testtask.data.utils.RequestResult
import com.github.mukiva.testtask.data.utils.dboAsNode
import com.github.mukiva.testtask.data.utils.map
import com.github.mukiva.testtask.database.NodeDatabase
import com.github.mukiva.testtask.database.models.NodeDbo
import com.github.mukiva.testtask.database.models.NodeWithChildrenRelation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

internal class NodeRepositoryImpl(
    private val nodeDatabase: NodeDatabase
): NodeRepository {
    override suspend fun getNode(id: String?): Flow<RequestResult<Node>> {
        val rootId = id ?: validateRootNode()
        val requestFlow = getNodeLocal(rootId)
        return requestFlow
            .map { requestResult ->
                requestResult.map { nodeWithChildren ->
                    dboAsNode(nodeWithChildren)
                }
            }
    }

    override suspend fun addNode(parentId: String) {
        val node = NodeDbo(
            id = HashGenerator.generate(),
            parentId = parentId
        )
        nodeDatabase.nodeDao
            .insertNode(node)
    }

    override suspend fun deleteNode(id: String) {
        nodeDatabase.nodeDao
            .deleteNodeRecursive(id)
    }

    private suspend fun validateRootNode(): String {
        val rootNode = nodeDatabase.nodeDao
            .getFirstRoot()
        val generatedId = HashGenerator.generate()
        if (rootNode == null) {
            nodeDatabase.nodeDao
                .insertNode(
                    NodeDbo(
                        id = generatedId,
                        parentId = null
                    )
                )
        }
        return rootNode?.id ?: generatedId
    }

    private suspend fun getNodeLocal(id: String): Flow<RequestResult<NodeWithChildrenRelation>> {
        val requestFlow = nodeDatabase.nodeDao
            .getNodeWithChildrenObservableById(id)
            .map { nodeWithChildren -> RequestResult.Success(nodeWithChildren) }
            .map { success -> success as RequestResult<NodeWithChildrenRelation> }
            .catch { cause ->
                Log.e("NodeRepositoryImpl", "${cause.stackTrace}")
                emit(RequestResult.Error(null, cause))
            }
        val start = flow<RequestResult<NodeWithChildrenRelation>> { emit(RequestResult.InProgress()) }
        return merge(start, requestFlow)
    }
}