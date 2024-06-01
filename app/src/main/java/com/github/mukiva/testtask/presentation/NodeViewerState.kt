package com.github.mukiva.testtask.presentation

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