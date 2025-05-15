package com.maxlift.presentation.ui.view.exercise

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxlift.presentation.ui.common.EditableTextField
import com.maxlift.presentation.ui.common.NonEditableTextFieldWithPopup
import com.maxlift.presentation.ui.view.camera.CameraViewModel
import com.maxlift.presentation.ui.view.person.SelectPersonPopUp
import com.maxlift.presentation.ui.view.person.list.PersonViewModel

@Composable
fun ExerciseEditScreen(
    viewModel: CameraViewModel,
    personViewModel: PersonViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    var personId by remember { mutableIntStateOf(sharedPreferences.getInt("person", 1)) }
    val person by personViewModel.personState.observeAsState()
    val times by viewModel.times.observeAsState()
    var weight by remember { mutableIntStateOf(sharedPreferences.getInt("weight", 50)) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(sharedPreferences.getString("type", "No type")) }

    LaunchedEffect(personId) {
        personViewModel.fetchPersonById(personId)
    }

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
                    keyboardType = KeyboardType.Text
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
                    maxLines = 4
                )
            }

            item {
                NonEditableTextFieldWithPopup(
                    value = person?.name ?: "",
                    onSelect = { newValue ->
                        personId = newValue.toInt()
                        sharedPreferences.edit().putInt("person", personId).apply()
                    },
                    label = "Person",
                    popupContent = { onDismiss, onSelect ->
                        SelectPersonPopUp(
                            onDismiss = onDismiss,
                            personViewModel = personViewModel,
                            onSelect = onSelect
                        )
                    }
                )
            }

            item {
                NonEditableTextFieldWithPopup(
                    value = "$type",
                    onSelect = { newValue ->
                        type = newValue
                        sharedPreferences.edit().putString("type", type).apply()
                    },
                    label = "Type",
                    popupContent = { onDismiss, onSelect ->
                        SelectTypePopUp(onDismiss = onDismiss, onSelect = onSelect)
                    }
                )
            }

            item {
                NonEditableTextFieldWithPopup(
                    value = "$weight kg",
                    onSelect = { newValue ->
                        weight = newValue.toInt()
                        sharedPreferences.edit().putInt("weight", weight).apply()
                    },
                    label = "Weight",
                    popupContent = { onDismiss, onSelect ->
                        SelectWeightPopUp(onDismiss = onDismiss, onSelect = onSelect)
                    }
                )
            }

            item {
                times?.let {
                    CustomBarChart(it)
                }
            }

            item {
                times?.let {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            "# of repetitions: ${it.size}",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            "Average time: ${it.average().toInt()} ms",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
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
                onClick = {
                    if (navController.currentDestination?.route == "result") {
                        viewModel.saveCurrentExercise(context)
                        navController.navigate("personInfo/$personId") {
                            popUpTo("persons") { inclusive = false }
                        }
                    }
                }
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