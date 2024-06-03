package com.github.mukiva.testtask.presentation

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.github.mukiva.testtask.data.utils.RequestResult
import com.github.mukiva.testtask.domain.Node
import com.github.mukiva.testtask.domain.usecase.LoadNodeUseCase
import com.github.mukiva.testtask.utils.AppDispatchers
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Stable
interface INodeViewerComponent {
    @Stable
    val state: StateFlow<NodeViewerState>
    @Stable
    val childListComponent: IChildListComponent
    @Stable
    val nodeAppBarComponent: INodeAppBarComponent
    @Stable
    fun retry()
}

internal class NodeViewerComponent @AssistedInject constructor(
    private val loadNodeUseCase: LoadNodeUseCase,
    childListComponentFactory: ChildListComponent.Factory,
    nodeAppBarComponentFactory: NodeAppBarComponent.Factory,
    @Assisted componentContext: ComponentContext,
    @Assisted private val nodeId: String?,
    @Assisted private val navigateToChild: (String) -> Unit,
    @Assisted private val navigateUp: () -> Unit
) : ComponentContext by componentContext, INodeViewerComponent {

    @AssistedFactory
    interface Factory {
        fun create(
            componentContext: ComponentContext,
            nodeId: String?,
            navigateUp: () -> Unit,
            navigateToChild: (String) -> Unit
        ): NodeViewerComponent
    }

    override val state: StateFlow<NodeViewerState>
        get() = mState.asStateFlow()

    override val childListComponent = childListComponentFactory.create(
        componentContext = childContext(CHILD_LIST_COMPONENT_KEY),
        navigateToChild = navigateToChild
    )

    override val nodeAppBarComponent = nodeAppBarComponentFactory.create(
        componentContext = childContext(NODE_APP_BAR_COMPONENT_KEY),
        navigateUp = navigateUp
    )

    private val mState = MutableStateFlow<NodeViewerState>(NodeViewerState.Loading)
    private val mComponentScope = CoroutineScope(AppDispatchers.default + SupervisorJob())
    private var mNodeLoaderJob: Job = createRequestJob()

    override fun retry() {
        mNodeLoaderJob.cancel()
        mNodeLoaderJob = createRequestJob()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun createRequestJob(): Job {
        return flow { emit(Unit) }
            .flatMapLatest { loadNodeUseCase(nodeId) }
            .onEach { requestResult -> notifyDataSetChanged(requestResult) }
            .launchIn(mComponentScope)
    }

    private fun asNodeViewerState(requestResult: RequestResult<Node>): NodeViewerState {
        return when (requestResult) {
            is RequestResult.Error -> NodeViewerState.Error
            is RequestResult.InProgress -> NodeViewerState.Loading
            is RequestResult.Success -> NodeViewerState.Content
        }
    }

    private fun notifyDataSetChanged(requestResult: RequestResult<Node>) {
        mState.tryEmit(asNodeViewerState(requestResult))
        childListComponent.submitList(requestResult)
        nodeAppBarComponent.updateAppBarState(requestResult)
    }

    companion object {
        private const val CHILD_LIST_COMPONENT_KEY = "CHILD_LIST_COMPONENT_KEY"
        private const val NODE_APP_BAR_COMPONENT_KEY = "NODE_APP_BAR_COMPONENT_KEY"
    }
}