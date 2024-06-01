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