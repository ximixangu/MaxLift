package com.maxlift.presentation.ui.view.person.list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.maxlift.domain.model.Person
import com.maxlift.presentation.ui.common.IconTextButton
import com.maxlift.presentation.ui.view.person.PersonCardItem
import kotlinx.coroutines.delay

@Composable
fun PersonListScreen(personViewModel: PersonViewModel, navController: NavController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route
    val personList by personViewModel.personListState.observeAsState(null)
    var shouldUpdate by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(shouldUpdate) {
        personViewModel.fetchAllPersons()
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

                            item {
                                AddPersonCardItem(
                                    onSave = { name ->
                                        personViewModel.savePerson(Person(0, name))
                                        shouldUpdate = true
                                    }
                                )
                            }

                            if (personList!!.isEmpty()) {
                                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                                    Text(
                                        text = "<----- Click here to register a new person",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            var isClicked by remember { mutableStateOf(false) }
            LaunchedEffect(isClicked) {
                delay(2000L)
                isClicked = false
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.TopCenter,
            ) {
                IconTextButton(
                    onClick = {
                        if (currentDestination == "persons" && !personList.isNullOrEmpty()){
                            navController.navigate("mlkit")
                        } else if (personList.isNullOrEmpty() && !isClicked) {
                            Toast.makeText(context, "You must first register a person", Toast.LENGTH_SHORT).show()
                            isClicked = true
                        }
                    },
                    text = "Register",
                    icon = Icons.Filled.RadioButtonChecked,
                    size = 35.dp,
                    color =
                        if (personList.isNullOrEmpty()) {
                            MaterialTheme.colorScheme.primary.copy(0.3f)
                        } else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}