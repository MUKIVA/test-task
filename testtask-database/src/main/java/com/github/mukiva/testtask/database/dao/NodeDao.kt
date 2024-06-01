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
    @Query("SELECT * FROM NodeDbo WHERE parent_id IS NULL LIMIT 1")
    suspend fun getFirstRoot(): NodeDbo?
    @Transaction
    @Query("SELECT * FROM NodeDbo WHERE id = :nodeId")
    fun getNodeWithChildrenById(nodeId: String): Flow<NodeWithChildrenRelation>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNode(node: NodeDbo)
    @Delete
    suspend fun deleteNode(node: NodeDbo)
}