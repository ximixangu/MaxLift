package com.maxlift.presentation.ui.feature.person.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxlift.domain.model.Exercise
import java.time.Instant
import java.util.Date

@Composable
fun PersonInfoScreen(personId: Int, navController: NavController) {
    val context = LocalContext.current
    val personInfoViewModel = PersonInfoViewModel()
    val person by personInfoViewModel.personState.observeAsState()
    val exerciseList by personInfoViewModel.exerciseListState.observeAsState()

    LaunchedEffect(Unit) {
        personInfoViewModel.fetchPersonAndExercises(context, personId)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if(person != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val name = person!!.name.split(" ").joinToString(" ") { word ->
                    word.replaceFirstChar { it.uppercase() }
                }

                PersonTitle(
                    name = name,
                    onClickDelete = {
                        personInfoViewModel.deletePerson(context, personId)
                        navController.navigateUp()
                    }
                )

                Spacer(Modifier.size(7.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .background(color = Color.Gray.copy(0.3f), shape = RoundedCornerShape(5.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Search...",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }

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

                //ExerciseCardItem(exercise = exerciseSample, person = person!!)
            }
        }
    }
}