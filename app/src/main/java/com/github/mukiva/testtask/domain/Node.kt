package com.github.mukiva.testtask.domain

data class Node(
    val id: String,
    val parentId: String?,
    val childList: List<Node>
) {
    val name: String
        get() = id.takeLast(20)
}