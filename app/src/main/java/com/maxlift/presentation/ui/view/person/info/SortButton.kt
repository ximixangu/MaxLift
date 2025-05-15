package com.maxlift.presentation.ui.view.person.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SortButton(
    onSelectSort: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.wrapContentSize()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Sort",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = -1f
                    }
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(80.dp)
                .background(color = MaterialTheme.colorScheme.secondaryContainer.copy(0.5f))
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Date",
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onClick = {
                    onSelectSort("id")
                    expanded = false
                },
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Weight",
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onClick = {
                    onSelectSort("weight")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Reps",
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onClick = {
                    onSelectSort("reps")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Type",
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                onClick = {
                    onSelectSort("type")
                    expanded = false
                }
            )
        }
    }
}