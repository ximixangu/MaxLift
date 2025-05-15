package com.maxlift.presentation.ui.view.person.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.maxlift.domain.model.Person

@Composable
fun PersonTitle(person: Person, onClickDelete: () -> Unit, onEdit: (Person) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    var showEditPersonPopup by remember { mutableStateOf(false) }
    var showDeletePersonPopup by remember { mutableStateOf(false) }
    val name = person.name.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }

    Surface(modifier = Modifier.fillMaxWidth().height(60.dp), shadowElevation = 5.dp) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )

            Row(modifier = Modifier.align(Alignment.CenterEnd)){
                Icon(
                    imageVector = Icons.Default.Edit, "",
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { showEditPersonPopup = true },
                    tint = MaterialTheme.colorScheme.secondary
                )
                Icon(
                    imageVector = Icons.Default.Delete, "",
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { showDeletePersonPopup = true },
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

    if(showEditPersonPopup) {
        EditPeronPopUp (
            person = person,
            onEdit = { onEdit(it) },
            onDismiss = { showEditPersonPopup = false }
        )
    }

    if(showDeletePersonPopup) {
        DeleteUserPopUp(
            onDelete = {
                onClickDelete()
            },
            onDismiss = { showDeletePersonPopup = false }
        )
    }
}