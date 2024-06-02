package com.github.mukiva.testtask.presentation

import com.arkivanov.decompose.ComponentContext
import com.github.mukiva.testtask.data.NodeRepository
import com.github.mukiva.testtask.data.utils.RequestResult
import com.github.mukiva.testtask.domain.Node
import com.github.mukiva.testtask.utils.AppDispatchers
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface IChildListComponent {
    val state: StateFlow<ChildListState>
    val navigateToChild: (String) -> Unit
    fun deleteNode(nodeId: String)
    fun submitList(requestResult: RequestResult<Node>)
}

internal class ChildListComponent @AssistedInject constructor(
    private val repository: NodeRepository,
    @Assisted componentContext: ComponentContext,
    @Assisted override val navigateToChild: (String) -> Unit,
) : ComponentContext by componentContext, IChildListComponent {
    @AssistedFactory
    interface Factory {
        fun create(
            componentContext: ComponentContext,
            navigateToChild: (String) -> Unit
        ): ChildListComponent
    }

    override val state: StateFlow<ChildListState>
        get() = mState.asStateFlow()

    private val mState = MutableStateFlow<ChildListState>(ChildListState.Empty)

    private val mComponentScope = CoroutineScope(AppDispatchers.default + SupervisorJob())

    override fun deleteNode(nodeId: String) {
        mComponentScope.launch(AppDispatchers.io) {
            repository.deleteNode(nodeId)
        }
    }

    override fun submitList(requestResult: RequestResult<Node>) {
        val data = requestResult.data ?: return
        mState.tryEmit(asChildListState(data.childList))
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