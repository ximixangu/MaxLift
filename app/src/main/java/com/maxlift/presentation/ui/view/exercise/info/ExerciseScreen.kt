package com.maxlift.presentation.ui.view.exercise.info

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
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxlift.presentation.ui.view.exercise.CustomBarChart
import com.maxlift.presentation.ui.view.exercise.ExerciseViewModel
import com.maxlift.presentation.ui.view.exercise.formatDate

@Composable
fun ExerciseScreen(
    id: Int,
    exerciseViewModel: ExerciseViewModel,
    navController: NavController
) {
    val exercise by exerciseViewModel.exercise.observeAsState()
    var showDeletePopUp by remember { mutableStateOf(false) }
    var showEditPopUp by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(id) {
        exerciseViewModel.fetchExercise(id)
    }

    Surface(modifier = Modifier.fillMaxSize())  {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
            ) {
                exercise?.let {
                    if(showEditPopUp) {
                        EditExercisePopUp (
                            exercise = it,
                            onEdit = {
                                exerciseViewModel.updateExercise(exercise!!)
                            },
                            onDismiss = { showEditPopUp = false }
                        )
                    }
                }

                if(showDeletePopUp) {
                    DeleteExercisePopUp(
                        onDelete = {
                            exerciseViewModel.deleteExercise(id)
                            navController.navigateUp()
                        },
                        onDismiss = { showDeletePopUp = false }
                    )
                }

                if (exercise != null) {
                    Spacer(Modifier.size(16.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (exercise!!.title != null && exercise!!.title!!.isNotBlank()) {
                            Text(
                                text = exercise!!.title!!,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f).padding(end = 1.dp),
                                maxLines = 2
                            )
                        } else {
                            Text(
                                text = exercise!!.type,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f),
                                maxLines = 2
                            )
                        }

                        Row {
                            Icon(
                                imageVector = Icons.Default.Edit, "",
                                modifier = Modifier
                                    .padding()
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) { showEditPopUp = true },
                                tint = MaterialTheme.colorScheme.secondary
                            )

                            Icon(
                                imageVector = Icons.Default.Delete, "",
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) { showDeletePopUp = true },
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    Text(
                        text = formatDate(exercise!!.date),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(Modifier.size(8.dp))

                    exercise!!.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify
                        )
                    }

                    Spacer(Modifier.size(16.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${exercise!!.type} ${exercise!!.times.size} x ${exercise!!.weight.toInt()}kg",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    Spacer(Modifier.size(10.dp))

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

                    Spacer(Modifier.size(8.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            "Fastest: ${exercise!!.times.min().toInt()} ms",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            "Slowest: ${exercise!!.times.max().toInt()} ms",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}