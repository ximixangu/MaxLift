package com.maxlift.presentation.ui.feature.tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun PersonListScreen(personViewModel: PersonViewModel) {
    val personList by personViewModel.personListState.observeAsState(null)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        personViewModel.fetchAllPersons(context)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),

    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                            PersonCardItem(
                                person = personList!![index],
                            )
                        }
                    }
                }
            }
        }
    }
}