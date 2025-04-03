package com.maxlift.presentation.ui.feature.person.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import com.maxlift.presentation.ui.feature.exercise.ExerciseCardItem
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .background(color = Color.Gray.copy(0.2f), shape = RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search, "",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Search by keyword...",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(Modifier.size(15.dp))

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

                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    ExerciseCardItem(exercise = exerciseSample, person = person!!)
                }
            }
        }
    }
}