package com.maxlift.presentation.ui.feature.person.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxlift.domain.model.Exercise
import com.maxlift.presentation.ui.feature.exercise.ExerciseCardItem

@Composable
fun ExerciseList(
    exerciseList: List<Exercise>?,
    navController: NavController,
    pageSize: Int = 20
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var currentPage by remember { mutableIntStateOf(0) }

    if (!exerciseList.isNullOrEmpty()) {
        val totalPages = (exerciseList.size + pageSize - 1) / pageSize
        if(currentPage > totalPages) currentPage = totalPages
        val start = currentPage * pageSize
        val end = minOf(start + pageSize, exerciseList.size)
        val pageItems = exerciseList.subList(start, end)

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(pageItems.size) { index ->
                    val exercise = pageItems[index]
                    ExerciseCardItem(exercise) {
                        if (navController.currentDestination?.route?.contains("personInfo") == true) {
                            keyboardController?.hide()
                            navController.navigate("exerciseInfo/${exercise.id}")
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.wrapContentSize(),
                shadowElevation = 8.dp
            ) {
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = { if (currentPage > 0) currentPage-- },
                        enabled = currentPage > 0,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            "",
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    Text(
                        text = "Page ${currentPage + 1} of $totalPages",
                        color = MaterialTheme.colorScheme.secondary
                    )

                    IconButton(
                        onClick = { if (currentPage < totalPages - 1) currentPage++ },
                        enabled = currentPage < totalPages - 1,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            "",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = -1f
                                }
                        )
                    }
                }
            }
        }
    }
}