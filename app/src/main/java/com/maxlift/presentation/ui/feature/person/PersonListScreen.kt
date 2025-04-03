package com.maxlift.presentation.ui.feature.person

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.maxlift.domain.model.Person
import com.maxlift.domain.usecase.person.SavePersonUseCase
import com.maxlift.presentation.ui.common.IconTextButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PersonListScreen(personViewModel: PersonViewModel, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route
    val personList by personViewModel.personListState.observeAsState(null)
    var shouldUpdate by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(shouldUpdate) {
        personViewModel.fetchAllPersons(context)
        shouldUpdate = false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Box(Modifier.fillMaxWidth().weight(1f)) {
                when {
                    personList == null -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    personList != null -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                AddPersonCardItem(
                                    onSave = { name ->
                                        coroutineScope.launch {
                                            withContext(Dispatchers.IO) {
                                                SavePersonUseCase.execute(context, Person(0, name))
                                            }
                                        }
                                        shouldUpdate = true
                                    }
                                )
                            }

                            items(personList!!.size) { index ->
                                val person = personList!![index]
                                PersonCardItem(
                                    person = person,
                                    onClick = {
                                        if (currentDestination == "persons") {
                                            navController.navigate(route = "personInfo/${person.id}")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.TopCenter,
            ) {
                IconTextButton(
                    onClick = { navController.navigate("mlkit") },
                    text = "Register",
                    icon = Icons.Filled.RadioButtonChecked
                )
            }
        }
    }
}