package com.maxlift.presentation.ui.view.exercise.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.maxlift.domain.model.Exercise
import com.maxlift.presentation.ui.common.EditableTextField

@Composable
fun EditExercisePopUp(
    exercise: Exercise,
    onDismiss: () -> Unit,
    onEdit: (Exercise) -> Unit,
) {
    var title by remember { mutableStateOf(exercise.title) }
    var description by remember { mutableStateOf(exercise.description) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(8.dp),
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Edit Exercise",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                EditableTextField(
                    value = title ?: "",
                    onValueChange = {
                        title = it
                    },
                    label = "Title",
                    keyboardType = KeyboardType.Text,
                    maxLines = 2
                )

                EditableTextField(
                    value = description ?: "",
                    onValueChange = {
                        description = it
                    },
                    label = "Description",
                    keyboardType = KeyboardType.Text,
                    maxLines = 5
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            exercise.title = title
                            exercise.description = description
                            onEdit(exercise)
                            onDismiss()
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}