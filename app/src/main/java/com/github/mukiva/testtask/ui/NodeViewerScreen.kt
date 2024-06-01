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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.mukiva.testtask.R
import com.github.mukiva.testtask.presentation.ChildListState
import com.github.mukiva.testtask.presentation.ChildState
import com.github.mukiva.testtask.presentation.NodeViewerComponent
import com.github.mukiva.testtask.presentation.NodeViewerState
import com.github.mukiva.testtask.uikit.ErrorScreen
import com.github.mukiva.testtask.uikit.LoadingScreen

@Composable
internal fun NodeViewerScreen(
    component: NodeViewerComponent,
    modifier: Modifier = Modifier
) {
    val state = component.state.collectAsState()

    NodeViewer(
        nodeViewerState = state.value,
        modifier = modifier,
        onDelete = { id -> component.deleteNode(id) },
        goToChild = { id -> component.navigateToChild(id) },
        onBackAction = component.navigateUp,
        onAddAction = { id -> component.addNode(id) }
    )
}

@Composable
@Preview(
    showBackground = true
)
internal fun NodeViewer(
    @PreviewParameter(NodeViewerStateParameterProvider::class)
    nodeViewerState: NodeViewerState,
    modifier: Modifier = Modifier,
    onDelete: (String) -> Unit = {},
    goToChild: (String) -> Unit = {},
    onBackAction: () -> Unit = {},
    onAddAction: (String) -> Unit = {}
) {
    when (nodeViewerState) {
        is NodeViewerState.Content ->
            NodeViewerContent(
                name = nodeViewerState.name,
                backButtonIsVisible = nodeViewerState.backButtonIsVisible,
                listState = nodeViewerState.childListState,
                onDelete = onDelete,
                goToChild = goToChild,
                onBackAction = onBackAction,
                onAddAction = { onAddAction(nodeViewerState.id) },
            )
        NodeViewerState.Error -> ErrorScreen()
        NodeViewerState.Loading -> LoadingScreen()
    }
}

@Composable
internal fun NodeViewerContent(
    name: String,
    backButtonIsVisible: Boolean,
    listState: ChildListState,
    modifier: Modifier = Modifier,
    onDelete: (String) -> Unit = {},
    goToChild: (String) -> Unit = {},
    onBackAction: () -> Unit = {},
    onAddAction: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            NodeViewerAppBar(
                name = name,
                backButtonIsVisible = backButtonIsVisible,
                onBackAction = onBackAction,
                onAddAction = onAddAction
            )
        }
    ) { paddingValues ->
        ChildList(
            listState = listState,
            modifier = modifier.padding(paddingValues),
            onDelete = onDelete,
            goToChild = goToChild
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NodeViewerAppBar(
    name: String,
    backButtonIsVisible: Boolean,
    modifier: Modifier = Modifier,
    onAddAction: () -> Unit = {},
    onBackAction: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = onAddAction) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_description)
                )
            }
        },
        navigationIcon = {
            if (backButtonIsVisible)
                IconButton(onClick = onBackAction) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.back_description)
                    )
                }
        },
        modifier = modifier
    )
}

internal class NodeViewerStateParameterProvider : PreviewParameterProvider<NodeViewerState> {
    override val values: Sequence<NodeViewerState>
        get() = sequenceOf(
            NodeViewerState.Error,
            NodeViewerState.Loading,
            NodeViewerState.Content(
                name = "asdasdasd",
                backButtonIsVisible = false,
                id = "",
                childListState = ChildListState.Empty
            ),
            NodeViewerState.Content(
                name = "asdasdasd",
                id = "",
                backButtonIsVisible = true,
                childListState = ChildListState.Content(
                    childList = listOf(
                        ChildState.Content(
                            id = "1",
                            name = "Test"
                        )
                    )
                )
            ),
        )

}

