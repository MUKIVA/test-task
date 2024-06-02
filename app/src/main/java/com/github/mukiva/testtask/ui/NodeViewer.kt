package com.github.mukiva.testtask.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.mukiva.testtask.R
import com.github.mukiva.testtask.data.utils.RequestResult
import com.github.mukiva.testtask.domain.Node
import com.github.mukiva.testtask.presentation.ChildListState
import com.github.mukiva.testtask.presentation.IChildListComponent
import com.github.mukiva.testtask.presentation.INodeAppBarComponent
import com.github.mukiva.testtask.presentation.INodeViewerComponent
import com.github.mukiva.testtask.presentation.NodeAppBarState
import com.github.mukiva.testtask.presentation.NodeViewerState
import com.github.mukiva.testtask.uikit.ErrorScreen
import com.github.mukiva.testtask.uikit.LoadingScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
@Preview(
    showBackground = true
)
internal fun NodeViewer(
    @PreviewParameter(NodeViewerComponentProvider::class)
    component: INodeViewerComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()

    when (state) {
        is NodeViewerState.Content ->
            NodeViewerContent(
                appBarComponent = component.nodeAppBarComponent,
                childListComponent = component.childListComponent,
                modifier = modifier
            )
        NodeViewerState.Error -> ErrorScreen(
            onRetry = component::retry
        )
        NodeViewerState.Loading -> LoadingScreen()
    }
}

@Composable
internal fun NodeViewerContent(
    childListComponent: IChildListComponent,
    appBarComponent: INodeAppBarComponent,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { NodeViewerAppBar(appBarComponent) }
    ) { paddingValues ->
        ChildList(
            component = childListComponent,
            modifier = modifier.padding(paddingValues),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NodeViewerAppBar(
    component: INodeAppBarComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.collectAsState()

    val instance = state as? NodeAppBarState.Content
        ?: return

    TopAppBar(
        title = {
            Text(
                text = instance.name,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = { component.addNode(instance.nodeId) }) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_description)
                )
            }
        },
        navigationIcon = {
            if (instance.backButtonIsVisible)
                IconButton(onClick = component.navigateUp ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.back_description)
                    )
                }
        },
        modifier = modifier
    )
}

internal class NodeViewerComponentProvider : PreviewParameterProvider<INodeViewerComponent> {
    override val values: Sequence<INodeViewerComponent>
        get() = sequenceOf(
            createMockComponent(NodeViewerState.Error),
            createMockComponent(NodeViewerState.Loading),
            createMockComponent(NodeViewerState.Content)
        )

    private fun createMockComponent(state: NodeViewerState): INodeViewerComponent {
        return object : INodeViewerComponent {
            override val state: StateFlow<NodeViewerState> =
                MutableStateFlow(state)
            override val childListComponent: IChildListComponent =
                createMockChildListComponent()
            override val nodeAppBarComponent: INodeAppBarComponent =
                createMockAppBarComponent(
                    NodeAppBarState.Content(
                        nodeId = "1",
                        name = "Original Name!",
                        backButtonIsVisible = true
                    )
                )

            override fun retry() {}
        }
    }

    private fun createMockAppBarComponent(state: NodeAppBarState): INodeAppBarComponent {
        return object : INodeAppBarComponent {
            override val state: StateFlow<NodeAppBarState>
                get() = MutableStateFlow(state)
            override val navigateUp: () -> Unit
                get() = {}

            override fun updateAppBarState(requestResult: RequestResult<Node>) {}

            override fun addNode(parentId: String) {}
        }
    }

    private fun createMockChildListComponent(): IChildListComponent {
        return object : IChildListComponent {
            override val state: StateFlow<ChildListState>
                get() = MutableStateFlow(ChildListState.Empty)
            override val navigateToChild: (String) -> Unit
                get() = {}

            override fun deleteNode(nodeId: String) {}

            override fun submitList(requestResult: RequestResult<Node>) {}
        }
    }
}
