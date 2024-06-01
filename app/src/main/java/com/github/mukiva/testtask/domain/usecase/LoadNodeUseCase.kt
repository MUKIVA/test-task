package com.github.mukiva.testtask.domain.usecase

import com.github.mukiva.testtask.data.NodeRepository
import com.github.mukiva.testtask.data.models.Node as DataNode
import com.github.mukiva.testtask.data.utils.RequestResult
import com.github.mukiva.testtask.data.utils.map
import com.github.mukiva.testtask.domain.Node
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadNodeUseCase @Inject constructor(
    private val nodeRepository: NodeRepository,
) {
    suspend operator fun invoke(
        nodeId: String? = null
    ): Flow<RequestResult<Node>> {
        return nodeRepository.getNode(nodeId)
            .map { requestResult ->
                requestResult.map(::asNode)
            }
    }

    private fun asNode(node: DataNode): Node {
        return Node(
            id = node.id,
            parentId = node.parentId,
            childList = node.children.map(::asNode)
        )
    }
}