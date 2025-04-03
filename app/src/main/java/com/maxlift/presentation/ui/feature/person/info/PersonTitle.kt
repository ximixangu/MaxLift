package com.maxlift.presentation.ui.feature.person.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PersonTitle(name: String, onClickDelete: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    var showDeletePersonPopup by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxWidth().height(60.dp), shadowElevation = 5.dp) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                imageVector = Icons.Default.Delete, "",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(5.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { showDeletePersonPopup = true },
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }

    if(showDeletePersonPopup) {
        DeleteUserPopUp(
            onDelete = { onClickDelete() },
            onDismiss = { showDeletePersonPopup = false }
        )
    }
}