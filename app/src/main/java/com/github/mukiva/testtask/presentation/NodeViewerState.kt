package com.github.mukiva.testtask.presentation

import androidx.compose.runtime.Immutable

@Immutable
sealed class NodeViewerState {
    @Immutable
    data object Loading : NodeViewerState()
    @Immutable
    data object Error : NodeViewerState()
    @Immutable
    data object Content : NodeViewerState()
}

@Immutable
sealed class NodeAppBarState {
    @Immutable
    data object Invisible : NodeAppBarState()
    @Immutable
    data class Content(
        val nodeId: String,
        val name: String,
        val backButtonIsVisible: Boolean
    ) : NodeAppBarState()
}