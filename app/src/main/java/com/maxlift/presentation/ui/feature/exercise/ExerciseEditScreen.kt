package com.maxlift.presentation.ui.feature.exercise

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maxlift.presentation.ui.feature.camera.CameraViewModel
import com.maxlift.presentation.ui.feature.person.SelectPersonPopUp

@Composable
fun ExerciseEditScreen(viewModel: CameraViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("personId", 0).apply()

    var personId by remember { mutableIntStateOf(sharedPreferences.getInt("personId", 0)) }
    val times by viewModel.times.observeAsState()
    val exercise by viewModel.exercise.observeAsState()
    var weight by remember { mutableIntStateOf(sharedPreferences.getInt("weight", 0)) }
    var title by remember { mutableStateOf(exercise?.title ?: "") }
    var description by remember { mutableStateOf(exercise?.title ?: "") }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(0.9f).weight(1f)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            horizontalArrangement = Arrangement.Center
        ) {
            item { Spacer(Modifier.size(16.dp)) }

            item{
                EditableTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        viewModel.setExerciseTitle(it)
                    },
                    label = "Title",
                    keyboardType = KeyboardType.Text,
                    focusManager = focusManager,
                    keyboardController = keyboardController
                )
            }

            item {
                NonEditableTextFieldWithPopup(
                    value = "$weight kg",
                    onSelect = { newValue ->
                        weight = newValue
                        sharedPreferences.edit().putInt("weight", weight).apply()
                    },
                    label = "Weight",
                    popupContent = { onDismiss, onSelect ->
                        SelectWeightPopUp(onDismiss = onDismiss, onSelect = onSelect)
                    }
                )
            }

            item {
                NonEditableTextFieldWithPopup(
                    value = "Nº${personId}",
                    onSelect = { newValue ->
                        personId = newValue
                        sharedPreferences.edit().putInt("personId", personId).apply()
                    },
                    label = "Person",
                    popupContent = { onDismiss, onSelect ->
                        SelectPersonPopUp(onDismiss = onDismiss, onSelect = onSelect)
                    }
                )
            }

            item {
                EditableTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        viewModel.setExerciseDescription(it)
                    },
                    label = "Description",
                    keyboardType = KeyboardType.Text,
                    maxLines = 4,
                    focusManager = focusManager,
                    keyboardController = keyboardController
                )
            }

            item {
                times?.let {
                    CustomBarChart(it)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier.fillMaxSize(0.7f),
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = Color.Transparent
                ),
                onClick = {}
            ) {
                Text(
                    text = "SAVE EXERCISE",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun EditableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    maxLines: Int = 1,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        singleLine = maxLines == 1,
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun NonEditableTextFieldWithPopup(
    value: String,
    label: String,
    onSelect: (Int) -> Unit,
    popupContent: @Composable (onDismiss: () -> Unit, onSelect: (Int) -> Unit) -> Unit
) {
    var showPopup by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    TextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        interactionSource = interactionSource,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

    if(interactionSource.collectIsPressedAsState().value) showPopup = true

    if (showPopup) {
        Surface {
            popupContent({showPopup = false}, onSelect)
        }
    }
}

//            Text(
//                "24/03/2024",
//                style = MaterialTheme.typography.titleSmall,
//                modifier = Modifier.padding(bottom = 20.dp)
//            )
//            Text(
//                "# of repetitions: ${it.size}",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(top = 20.dp)
//            )
//            Text(
//                "Average time: ${it.average().toInt()} ms",
//                style = MaterialTheme.typography.bodyLarge,
//            )