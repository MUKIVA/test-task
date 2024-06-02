package com.github.mukiva.testtask.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.mukiva.testtask.data.utils.RequestResult
import com.github.mukiva.testtask.domain.Node
import com.github.mukiva.testtask.presentation.ChildListState
import com.github.mukiva.testtask.presentation.ChildState
import com.github.mukiva.testtask.presentation.IChildListComponent
import com.github.mukiva.testtask.uikit.EmptyScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
@Preview(
    showBackground = true
)
internal fun ChildList(
    @PreviewParameter(ChildListComponentProvider::class)
    component: IChildListComponent,
    modifier: Modifier = Modifier,
) {
    val listState by component.state.collectAsState()

    when (val instance = listState) {
        is ChildListState.Content -> ChildListContent(
            childList = instance.childList,
            modifier = modifier,
            onDelete = component::deleteNode,
            goToChild = component.navigateToChild
        )
        ChildListState.Empty -> EmptyScreen(modifier)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ChildListContent(
    childList: List<ChildState>,
    modifier: Modifier = Modifier,
    onDelete: (String) -> Unit = {},
    goToChild: (String) -> Unit = {}
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = childList,
            key = { childState -> childState.id }
        ) { childState ->
            Column(modifier = Modifier.animateItemPlacement()) {
                Child(
                    childState = childState,
                    onDelete = { onDelete(childState.id) },
                    goToChild = { goToChild(childState.id) }
                )
                HorizontalDivider()
            }
        }
    }
}

internal class ChildListComponentProvider : PreviewParameterProvider<IChildListComponent> {

    override val values: Sequence<IChildListComponent>
        get() = sequenceOf(
            createMockComponent(ChildListState.Empty),
            createMockComponent(ChildListState.Content(
                childList = listOf(
                    ChildState.Content("1", "Child 1"),
                    ChildState.Content("2", "Child 2"),
                    ChildState.InProgress("3"),
                    ChildState.Content("4", "Child 4")
                )
            ))
        )

    private fun createMockComponent(listState: ChildListState): IChildListComponent {
        return object : IChildListComponent {
            override val state: StateFlow<ChildListState> =
                MutableStateFlow(listState)
            override val navigateToChild: (String) -> Unit = {}

            override fun deleteNode(nodeId: String) {}

            override fun submitList(requestResult: RequestResult<Node>) {}
        }
    }
}