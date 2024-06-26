package com.github.mukiva.testtask.di

import android.content.Context
import com.github.mukiva.testtask.data.NodeRepository
import com.github.mukiva.testtask.data.createNodeRepository
import com.github.mukiva.testtask.database.NodeDatabase
import com.github.mukiva.testtask.database.createNodeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideNodeDatabase(
        @ApplicationContext applicationContext: Context
    ): NodeDatabase = createNodeDatabase(applicationContext)

    @Provides
    @Singleton
    fun provideNodeRepository(
        nodeDatabase: NodeDatabase
    ): NodeRepository = createNodeRepository(nodeDatabase)

}