package com.github.mukiva.testtask.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(
    showSystemUi = true
)
fun EmptyScreen(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.list_state_empty_message)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}