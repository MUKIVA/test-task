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
import kotlinx.coroutines.launch

interface INodeAppBarComponent {
    val state: StateFlow<NodeAppBarState>
    val navigateUp: () -> Unit
    fun updateAppBarState(requestResult: RequestResult<Node>)
    fun addNode(parentId: String)
}

internal class NodeAppBarComponent @AssistedInject constructor(
    private val nodeRepository: NodeRepository,
    @Assisted componentContext: ComponentContext,
    @Assisted override val navigateUp: () -> Unit
) : ComponentContext by componentContext, INodeAppBarComponent {

    @AssistedFactory
    interface Factory {
        fun create(
            componentContext: ComponentContext,
            navigateUp: () -> Unit
        ): NodeAppBarComponent
    }

    override val state: StateFlow<NodeAppBarState>
        get() = mState


    private val mComponentScope = CoroutineScope(AppDispatchers.default + SupervisorJob())
    private val mState = MutableStateFlow<NodeAppBarState>(NodeAppBarState.Invisible)

    override fun updateAppBarState(requestResult: RequestResult<Node>) {
        mState.tryEmit(asAppBarState(requestResult))
    }

    override fun addNode(parentId: String) {
        mComponentScope.launch(AppDispatchers.io) {
            nodeRepository.addNode(parentId)
        }
    }

    private fun asAppBarState(
        requestResult: RequestResult<Node>
    ): NodeAppBarState {
        return when (requestResult) {
            is RequestResult.Success -> {
                val data = checkNotNull(requestResult.data)
                NodeAppBarState.Content(
                    nodeId = data.id,
                    name = data.name,
                    backButtonIsVisible = data.parentId != null
                )
            }
            else -> NodeAppBarState.Invisible
        }
    }
}