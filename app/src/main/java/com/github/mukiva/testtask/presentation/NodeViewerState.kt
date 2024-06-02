package com.github.mukiva.testtask.presentation

sealed class NodeViewerState {
    data object Loading : NodeViewerState()
    data object Error : NodeViewerState()
    data object Content : NodeViewerState()
}

sealed class NodeAppBarState {
    data object Invisible : NodeAppBarState()
    data class Content(
        val nodeId: String,
        val name: String,
        val backButtonIsVisible: Boolean
    ) : NodeAppBarState()
}