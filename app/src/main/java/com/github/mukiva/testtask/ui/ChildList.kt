package com.github.mukiva.testtask.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.github.mukiva.testtask.presentation.ChildListState
import com.github.mukiva.testtask.presentation.ChildState
import com.github.mukiva.testtask.uikit.EmptyScreen

@Composable
@Preview(
    showBackground = true
)
internal fun ChildList(
    @PreviewParameter(ChildListStateProvider::class)
    listState: ChildListState,
    modifier: Modifier = Modifier,
    onDelete: (String) -> Unit = {},
    goToChild: (String) -> Unit = {}

) {
    when (listState) {
        is ChildListState.Content -> ChildListContent(
            childList = listState.childList,
            modifier = modifier,
            onDelete = onDelete,
            goToChild = goToChild
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

internal class ChildListStateProvider : PreviewParameterProvider<ChildListState> {
    override val values: Sequence<ChildListState>
        get() = sequenceOf(
            ChildListState.Empty,
            ChildListState.Content(
                childList = listOf(
                    ChildState.Content("1", "Samle name 1"),
                    ChildState.Content("2", "Samle name 2"),
                    ChildState.Content("3", "Samle name 3"),
                    ChildState.InProgress("4"),
                    ChildState.Content("5", "Samle name 3"),
                )
            )
        )

}