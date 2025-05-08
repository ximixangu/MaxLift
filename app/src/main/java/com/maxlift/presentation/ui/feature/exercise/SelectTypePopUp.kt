package com.maxlift.presentation.ui.feature.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SelectTypePopUp(
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    var customType by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp),
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Select Exercise Type",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val types = listOf("Bench Press", "Squat", "Half Squat", "Dead Lift")

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        types.take(2).forEach { type ->
                            TypeButton(type, onSelect, onDismiss)
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        types.drop(2).forEach { type ->
                            TypeButton(type, onSelect, onDismiss)
                        }
                    }
                }

                OutlinedTextField(
                    value = customType,
                    onValueChange = { customType = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Custom Type") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (customType.isNotBlank()) {
                                onSelect(customType)
                                onDismiss()
                            }
                        }
                    )
                )

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Button(
                        onClick = { onDismiss() },
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if(customType.isNotBlank()) onSelect(customType)
                            onDismiss()
                        },
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
private fun TypeButton(
    type: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Text(
        text = type,
        modifier = Modifier
            .clickable {
                onSelect(type)
                onDismiss()
            }
            .fillMaxWidth()
            .background(
                color = Color.Transparent
            ),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textAlign = TextAlign.Center
    )
}
