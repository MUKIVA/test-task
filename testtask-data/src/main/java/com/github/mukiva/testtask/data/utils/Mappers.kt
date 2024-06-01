package com.github.mukiva.testtask.data.utils

import com.github.mukiva.testtask.data.models.Node
import com.github.mukiva.testtask.database.models.NodeDbo
import com.github.mukiva.testtask.database.models.NodeWithChildrenRelation

internal fun nodeAsDbo(node: Node): NodeDbo {
    return NodeDbo(
        id = node.id,
        parentId = node.parentId
    )
}

internal fun dboAsNode(dbo: NodeDbo): Node {
    return Node(
        id = dbo.id,
        parentId = dbo.parentId,
        children = emptyList()
    )
}

internal fun dboAsNode(dbo: NodeWithChildrenRelation): Node {
    return Node(
        id = dbo.node.id,
        parentId = dbo.node.parentId,
        children = dbo.children.map(::dboAsNode)
    )
}