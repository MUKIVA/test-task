package com.github.mukiva.testtask.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.retainedComponent
import com.github.mukiva.testtask.presentation.AppComponent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainComponentFactory: AppComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val component = retainedComponent(factory = mainComponentFactory::create)

        setContent {
            MaterialTheme {
                AppContent(
                    component = component,
                    Modifier.fillMaxSize()
                )
            }
        }
    }
}