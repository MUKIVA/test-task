package com.github.mukiva.testtask.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(
    showSystemUi = true
)
fun ErrorScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.list_state_error_message),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.default_spacer_size)))
        FilledTonalButton(onClick = onRefresh) {
            Text(stringResource(R.string.button_retry))
        }
    }
}