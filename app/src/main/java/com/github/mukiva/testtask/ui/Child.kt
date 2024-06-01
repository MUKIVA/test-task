package com.github.mukiva.testtask.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.github.mukiva.testtask.presentation.ChildState

@Composable
@Preview(showBackground = true)
internal fun Child(
    @PreviewParameter(ChildStatePreviewProvider::class)
    childState: ChildState,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    goToChild: () -> Unit = {}
) {
    Crossfade(childState, label = "Child") { state ->
        when (state) {
            is ChildState.Content -> ChildContent(
                name = state.name,
                modifier = Modifier.height(50.dp),
                onClick = goToChild,
                onDelete = onDelete
            )
            is ChildState.InProgress -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
internal fun ChildContent(
    name: String,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 16.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1.0f)
        )
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null
            )
        }
    }
}

internal class ChildStatePreviewProvider : PreviewParameterProvider<ChildState> {
    override val values: Sequence<ChildState>
        get() = sequenceOf(
            ChildState.Content(
                id = "",
                name = "iuiajeyr92271983yljkhALSKJH"
            ),
            ChildState.Content(
                id = "",
                name = "iuiajeyr92271983yljkhALSKJHiuiajeyr92271983yljkhALSKJHiuiajeyr92271983yljkhALSKJH"
            ),
            ChildState.InProgress(
                id = ""
            )
        )
}