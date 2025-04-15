package com.maxlift.presentation.ui.feature.person.info

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxlift.presentation.ui.feature.exercise.ExerciseCardItem

@Composable
fun PersonInfoScreen(personId: Int, personInfoViewModel: PersonInfoViewModel, navController: NavController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val person by personInfoViewModel.personState.observeAsState()
    val exerciseList by personInfoViewModel.exerciseListState.observeAsState()
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    var minWeight by remember { mutableIntStateOf(0) }
    var maxWeight by remember { mutableIntStateOf(Int.MAX_VALUE) }
    var minReps by remember { mutableIntStateOf(0) }
    var maxReps by remember { mutableIntStateOf(Int.MAX_VALUE) }
    var title by remember { mutableStateOf("") }

    sharedPreferences.edit().putInt("person", personId).apply()

    LaunchedEffect(Unit) {
        personInfoViewModel.fetchPersonAndExercises(context, personId)
    }

    LaunchedEffect(title, minReps, maxReps, minWeight, maxWeight) {
        personInfoViewModel.fetchExercisesSearch(
            id = personId,
            context = context,
            title = title,
            minReps = minReps,
            maxReps = maxReps,
            maxWeight = maxWeight,
            minWeight = minWeight
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if(person != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PersonTitle(
                    person = person!!,
                    onEdit = {
                        personInfoViewModel.editPerson(context, it)
                        personInfoViewModel.fetchPersonAndExercises(context, it.id)
                    },
                    onClickDelete = {
                        personInfoViewModel.deletePerson(context, personId)
                        navController.navigateUp()
                    }
                )

                Spacer(Modifier.size(11.dp))

                SearchBar {
                    title = it
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer(Modifier.fillMaxWidth(0.05f))
                    FilterButton(
                        text = "Weight",
                        appendableText = "kg",
                        onFilter = { lower, upper ->
                            minWeight = lower ?: 0
                            maxWeight = upper ?: Int.MAX_VALUE
                        }
                    )
                    Spacer(Modifier.size(8.dp))
                    FilterButton(
                        text = "Repetitions",
                        appendableText = "reps",
                        onFilter = { lower, upper ->
                            minReps = lower ?: 0
                            maxReps = upper ?: Int.MAX_VALUE
                        }
                    )
                }

                Surface(modifier = Modifier.wrapContentSize(), shadowElevation = 5.dp) {
                    Box(
                        Modifier.fillMaxSize().weight(1f),
                        contentAlignment = Alignment.Center,
                    ){
                        if (exerciseList != null && exerciseList!!.isNotEmpty()) {
                            LazyColumn(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                exerciseList?.let {
                                    items(it.size) { index ->
                                        val exercise = it[index]
                                        ExerciseCardItem(exercise) {
                                            if (navController.currentDestination?.route?.contains("personInfo") == true) {
                                                keyboardController?.hide()
                                                navController.navigate("exerciseInfo/${exercise.id}")
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "No Exercises Registered",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.TopCenter),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}