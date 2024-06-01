package com.github.mukiva.testtask.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

internal class AppComponent @AssistedInject constructor(
    private val nodeViewerComponentFactory: NodeViewerComponent.Factory,
    @Assisted componentContext: ComponentContext
) : ComponentContext by componentContext {
    @AssistedFactory
    interface Factory {
        fun create(
            componentContext: ComponentContext
        ): AppComponent
    }
    sealed class Child {
        data class NodeViewer(val component: NodeViewerComponent) : Child()
    }
    @Serializable
    sealed class ChildConfig {
        @Serializable
        data class NodeViewer(val nodeId: String? = null) : ChildConfig()
    }

    private val mNavigation = StackNavigation<ChildConfig>()

    val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = mNavigation,
            initialConfiguration = ChildConfig.NodeViewer(),
            serializer = ChildConfig.serializer(),
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun createChild(
        config: ChildConfig,
        component: ComponentContext
    ): Child = when (config) {
        is ChildConfig.NodeViewer ->
            Child.NodeViewer(nodeViewerComponentFactory
                .create(
                    componentContext = component,
                    nodeId = config.nodeId,
                    navigateToChild = { id -> mNavigation.push(ChildConfig.NodeViewer(id)) },
                    navigateUp = { mNavigation.pop() }
                )
            )
    }

}