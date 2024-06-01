package com.github.mukiva.testtask.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NodeDbo(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("parent_id")
    val parentId: String? = null
)