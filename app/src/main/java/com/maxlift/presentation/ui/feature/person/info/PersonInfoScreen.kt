package com.maxlift.presentation.ui.feature.person.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.maxlift.domain.model.Exercise
import com.maxlift.presentation.ui.feature.exercise.ExerciseCardItem
import java.time.Instant
import java.util.Date

@Composable
fun PersonInfoScreen(personId: Int) {
    val context = LocalContext.current
    val personInfoViewModel = PersonInfoViewModel()
    val person by personInfoViewModel.personState.observeAsState()
    val exerciseList by personInfoViewModel.exerciseListState.observeAsState()

    LaunchedEffect(Unit) {
        personInfoViewModel.fetchPersonAndExercises(context, personId)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if(person != null) {
            Column(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val exerciseSample = Exercise(
                        id = 0,
                        type = "Press Banca",
                        personId = 0,
                        weight = 80.0f,
                        times = listOf(100f, 110f, 110f, 100f),
                        date = Date.from(Instant.now()),
                        numberOfRepetitions = 4,
                        description = "Exercici d'exemple per la card item"
                    )

                    ExerciseCardItem(exercise = exerciseSample, person = person!!)
                }
            }
        }
    }
}