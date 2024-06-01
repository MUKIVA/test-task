package com.github.mukiva.testtask.database

import android.content.Context
import androidx.room.Room
import com.github.mukiva.testtask.database.dao.NodeDao

class NodeDatabase internal constructor(
    private val nodeDatabaseImpl: NodeDatabaseImpl
) {
    val nodeDao: NodeDao
        get() = nodeDatabaseImpl.nodeDao()
}


fun createNodeDatabase(
    context: Context
): NodeDatabase {
    val applicationContext = context.applicationContext
        ?: error("Fail to get the Application Context")
    val databaseImpl = Room.databaseBuilder(
        context = applicationContext,
        klass = NodeDatabaseImpl::class.java,
        name = "node"
    )
        .build()
    return NodeDatabase(databaseImpl)
}