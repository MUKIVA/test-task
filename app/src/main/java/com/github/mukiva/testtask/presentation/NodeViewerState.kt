package com.github.mukiva.testtask.presentation

sealed interface ChildState {
    val id: String
    data class InProgress(
        override val id: String
    ) : ChildState
    data class Content(
        override val id: String,
        val name: String
    ) : ChildState
}

sealed class ChildListState {
    data object Empty : ChildListState()
    data class Content(
        val childList: List<ChildState>
    ) : ChildListState()
}

sealed class NodeViewerState {
    data object Loading : NodeViewerState()
    data object Error : NodeViewerState()
    data class Content(
        val id: String,
        val name: String,
        val backButtonIsVisible: Boolean,
        val childListState: ChildListState
    ) : NodeViewerState()
}