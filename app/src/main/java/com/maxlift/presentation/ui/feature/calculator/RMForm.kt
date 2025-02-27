package com.maxlift.presentation.ui.feature.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maxlift.domain.usecase.rmCompute.RMParameters

@Composable
fun RMForm() {
    val keyboardController = LocalSoftwareKeyboardController.current
    var rmParameters by remember { mutableStateOf(RMParameters()) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .width(300.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "RM Calculator",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            TextField(
                value = rmParameters.weight,
                onValueChange = { data ->
                    rmParameters = rmParameters.copy(weight = data)
                },
                label = { Text("Weight (kgs)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                modifier = Modifier.padding(vertical = 3.dp)
            )

            val genderOptions = listOf("Male", "Female", "Other")
            var expanded by remember { mutableStateOf(false) }

            Box() {
                OutlinedTextField(
                    value = rmParameters.gender,
                    onValueChange = {},
                    label = { Text("Gender") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Gender DropDown Menu"
                            )
                        }
                    },
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genderOptions.forEach { gender ->
                        DropdownMenuItem(
                            text = { Text(gender) },
                            onClick = {
                                rmParameters.gender = gender
                                expanded = false
                            }
                        )
                    }
                }
            }

            TextField(
                value = rmParameters.repetitions,
                onValueChange = { data ->
                    rmParameters = rmParameters.copy(repetitions = data)
                },
                label = { Text("Number of repetitions") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Button(
                enabled = rmParameters.isValid(),
                onClick = {
                    keyboardController?.hide()
                    println("Weight: ${rmParameters.weight}, Gender: ${rmParameters.gender}, Reps: ${rmParameters.repetitions}")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Compute RM")
            }
        }
    }
}
