package com.github.mukiva.testtask.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class NodeWithChildrenRelation(
    @Embedded
    val node: NodeDbo,

    @Relation(
        parentColumn = "id",
        entityColumn = "parent_id"
    )
    val children: List<NodeDbo>
)