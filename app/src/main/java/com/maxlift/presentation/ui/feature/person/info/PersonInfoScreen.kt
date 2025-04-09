package com.maxlift.presentation.ui.feature.person.info

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxlift.presentation.ui.feature.exercise.ExerciseCardItem

@Composable
fun PersonInfoScreen(personId: Int, navController: NavController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val personInfoViewModel = PersonInfoViewModel()
    val person by personInfoViewModel.personState.observeAsState()
    val exerciseList by personInfoViewModel.exerciseListState.observeAsState()
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    sharedPreferences.edit().putInt("person", personId).apply()

    LaunchedEffect(person) {
        personInfoViewModel.fetchPersonAndExercises(context, personId)
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
                    personInfoViewModel.fetchExercisesByPersonAndTitle(context, personId, it)
                }

                Box(
                    Modifier.fillMaxSize().weight(1f),
                    contentAlignment = Alignment.Center
                ){
                    if (exerciseList != null) {
                        LazyColumn(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item { Spacer(Modifier.size(3.dp)) }
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
                    }
                }
            }
        }
    }
}