package com.maxlift.presentation.ui.feature.person.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.maxlift.presentation.ui.common.EditableTextField

@Composable
fun FilterButton(
    text: String,
    appendableText: String?,
    onFilter: (Int?, Int?) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var currentLower: String by remember { mutableStateOf("") }
    var currentUpper: String by remember { mutableStateOf("") }

    Surface(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(0.4f)),
        color = Color.White,
        shadowElevation = 0.dp,
        onClick = { showDialog = true }
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (currentLower.isEmpty() && currentUpper.isEmpty()) {
                    text
                } else if (currentLower.isEmpty()) {
                    "<$currentUpper $appendableText"
                } else if (currentUpper.isEmpty()) {
                    ">$currentLower $appendableText"
                } else {
                    "$currentLower - $currentUpper $appendableText"
                },
                style = MaterialTheme.typography.labelSmall,
            )
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = {
                    showDialog = false
                    onFilter(currentLower.toIntOrNull(), currentUpper.toIntOrNull())
                }
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .heightIn(max = 400.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(0.8f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Select $text Range",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        EditableTextField(
                            value = currentLower,
                            label = "From",
                            onValueChange = { currentLower = it },
                            keyboardType = KeyboardType.Number
                        )
                        EditableTextField(
                            value = currentUpper,
                            label = "To",
                            onValueChange = { currentUpper = it },
                            keyboardType = KeyboardType.Number
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            TextButton(
                                onClick = {
                                    currentLower = ""
                                    currentUpper = ""
                                    onFilter(currentLower.toIntOrNull(), currentUpper.toIntOrNull())
                                    showDialog = false
                                },
                                content = { Text("Reset", style = MaterialTheme.typography.titleMedium) },
                            )
                            TextButton(
                                onClick = {
                                    onFilter(currentLower.toIntOrNull(), currentUpper.toIntOrNull())
                                    showDialog = false
                                },
                                content = { Text("OK", style = MaterialTheme.typography.titleMedium) },
                            )
                        }
                    }
                }
            }
        }
    }
}