package com.github.mukiva.testtask.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.mukiva.testtask.database.dao.NodeDao
import com.github.mukiva.testtask.database.models.NodeDbo

@Database(
    entities = [
        NodeDbo::class
    ],
    version = 0,
    exportSchema = false
)
internal abstract class NodeDatabaseImpl() : RoomDatabase() {
    abstract fun nodeDao(): NodeDao
}