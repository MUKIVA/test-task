package com.github.mukiva.testtask.data.models

data class Node(
    val id: String,
    val parentId: String?,
    val children: List<Node>
)