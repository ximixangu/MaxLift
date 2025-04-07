package com.maxlift.presentation.ui.feature.exercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ExerciseScreen(id: Int, navController: NavController) {
    val context = LocalContext.current
    val exerciseViewModel: ExerciseViewModel = viewModel()
    val exercise by exerciseViewModel.exercise.observeAsState()
    var showDeletePopUp by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(id) {
        exerciseViewModel.fetchExercise(context, id)
    }

    Surface(modifier = Modifier.fillMaxSize())  {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
            ) {
                if(showDeletePopUp) {
                    DeleteExercisePopUp(
                        onDelete = {
                            exerciseViewModel.deleteExercise(context, id)
                            navController.navigateUp()
                        },
                        onDismiss = { showDeletePopUp = false }
                    )
                }

                if (exercise != null) {
                    Spacer(Modifier.size(16.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (exercise!!.title != null && exercise!!.title!!.isNotBlank()) {
                            Text(
                                text = exercise!!.title!!,
                                style = MaterialTheme.typography.titleLarge,
                            )
                        } else {
                            Text(
                                text = exercise!!.type,
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Delete, "",
                            modifier = Modifier
                                .padding(5.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) { showDeletePopUp = true },
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Text(
                        text = "${exercise!!.type} " + formatDate(exercise!!.date),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(Modifier.size(8.dp))

                    exercise!!.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(Modifier.size(16.dp))

                    CustomBarChart(exercise!!.times.map { it.toInt() })

                    Spacer(Modifier.size(16.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            "# of repetitions: ${exercise!!.times.size}",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            "Average time: ${exercise!!.times.average().toInt()} ms",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}