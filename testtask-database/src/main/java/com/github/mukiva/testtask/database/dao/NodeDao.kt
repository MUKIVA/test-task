package com.github.mukiva.testtask.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.mukiva.testtask.database.models.NodeDbo
import com.github.mukiva.testtask.database.models.NodeWithChildrenRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface NodeDao {
    @Query("SELECT * FROM nodedbo WHERE parent_id IS NULL LIMIT 1")
    suspend fun getFirstRoot(): NodeDbo?
    @Transaction
    @Query("SELECT * FROM nodedbo WHERE id = :nodeId")
    fun getNodeWithChildrenObservableById(nodeId: String): Flow<NodeWithChildrenRelation>
    @Transaction
    @Query("SELECT * FROM nodedbo WHERE id = :nodeId")
    suspend fun getNodeWithChildrenById(nodeId: String): NodeWithChildrenRelation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNode(node: NodeDbo)
    @Query("DELETE FROM nodedbo WHERE id = :nodeId")
    suspend fun deleteNodeById(nodeId: String)
    @Transaction
    suspend fun deleteNodeRecursive(nodeId: String) {
        val nodeWithChildren = getNodeWithChildrenById(nodeId)
        nodeWithChildren.children.onEach { node ->
            deleteNodeRecursive(node.id)
        }
        deleteNodeById(nodeId)
    }
}