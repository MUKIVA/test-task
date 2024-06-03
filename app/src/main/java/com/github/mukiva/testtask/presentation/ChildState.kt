package com.github.mukiva.testtask.presentation

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ChildState {
    val id: String
    @Immutable
    data class InProgress(
        override val id: String
    ) : ChildState
    @Immutable
    data class Content(
        override val id: String,
        val name: String
    ) : ChildState
}