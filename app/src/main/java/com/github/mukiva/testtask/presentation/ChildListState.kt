package com.github.mukiva.testtask.presentation

sealed class ChildListState {
    data object Empty : ChildListState()
    data class Content(
        val childList: List<ChildState>
    ) : ChildListState()
}