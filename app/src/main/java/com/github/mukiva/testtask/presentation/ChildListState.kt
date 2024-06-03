package com.github.mukiva.testtask.presentation

import androidx.compose.runtime.Immutable

@Immutable
sealed class ChildListState {
    @Immutable
    data object Empty : ChildListState()
    @Immutable
    data class Content(
        val childList: List<ChildState>
    ) : ChildListState()
}