package com.github.mukiva.testtask.presentation

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.github.mukiva.testtask.data.NodeRepository
import com.github.mukiva.testtask.data.utils.RequestResult
import com.github.mukiva.testtask.domain.Node
import com.github.mukiva.testtask.domain.usecase.LoadNodeUseCase
import com.github.mukiva.testtask.utils.AppDispatchers
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class NodeViewerComponent @AssistedInject constructor(
    private val loadNodeUseCase: LoadNodeUseCase,
    private val repository: NodeRepository,
    @Assisted componentContext: ComponentContext,
    @Assisted nodeId: String?,
    @Assisted val navigateToChild: (String) -> Unit,
    @Assisted val navigateUp: () -> Unit
) : ComponentContext by componentContext {

    @AssistedFactory
    interface Factory {
        fun create(
            componentContext: ComponentContext,
            nodeId: String?,
            navigateUp: () -> Unit,
            navigateToChild: (String) -> Unit
        ): NodeViewerComponent
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<NodeViewerState> by lazy {
        flow { emit(Unit) }
            .flatMapLatest { loadNodeUseCase(nodeId) }
            .map(::asNodeViewerState)
            .onEach { Log.d("STATE", "$it") }
            .stateIn(
                scope = mComponentScope,
                started = SharingStarted.Lazily,
                initialValue = NodeViewerState.Loading
            )
    }

    private val mComponentScope = CoroutineScope(AppDispatchers.default + SupervisorJob())

    fun addNode(parentId: String) {
        mComponentScope.launch(AppDispatchers.io) {
            repository.addNode(parentId)
        }
    }

    fun deleteNode(nodeId: String) {
        mComponentScope.launch(AppDispatchers.io) {
            repository.deleteNode(nodeId)
        }
    }

    private fun asNodeViewerState(requestResult: RequestResult<Node>): NodeViewerState {
        return when (requestResult) {
            is RequestResult.Error -> NodeViewerState.Error
            is RequestResult.InProgress -> NodeViewerState.Loading
            is RequestResult.Success -> {
                val data = checkNotNull(requestResult.data)
                NodeViewerState.Content(
                    id = data.id,
                    name = data.name,
                    backButtonIsVisible = data.parentId != null,
                    childListState = asChildListState(data.childList)
                )
            }
        }
    }

    private fun asChildListState(list: List<Node>): ChildListState {
        return when {
            list.isEmpty() -> ChildListState.Empty
            else -> ChildListState.Content(
                childList = list.map(::asChildState)
            )
        }
    }

    private fun asChildState(node: Node): ChildState {
        return ChildState.Content(
            id = node.id,
            name = node.name
        )
    }
}