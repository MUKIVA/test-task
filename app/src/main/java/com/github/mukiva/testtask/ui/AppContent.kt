package com.github.mukiva.testtask.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.github.mukiva.testtask.presentation.AppComponent

@Composable
internal fun AppContent(
    component: AppComponent,
    modifier: Modifier = Modifier
) {
    val stack = component.stack.subscribeAsState()
    Children(
        stack = stack.value,
        modifier = modifier.fillMaxSize(),
        animation = stackAnimation(slide())
    ) { createdChild ->
        when (val instance = createdChild.instance) {
            is AppComponent.Child.NodeViewer ->
                NodeViewer(instance.component)
        }
    }
}