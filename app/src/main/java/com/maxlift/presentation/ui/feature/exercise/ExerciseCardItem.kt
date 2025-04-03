package com.maxlift.presentation.ui.feature.exercise

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maxlift.domain.model.Exercise
import com.maxlift.domain.model.Person
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExerciseCardItem(exercise: Exercise, person: Person) {
    Surface(
        modifier = Modifier.wrapContentSize(),
        shadowElevation = 10.dp
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(180.dp),
        ) {
            Column(Modifier.fillMaxSize()) {

            }
        }
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
    return formatter.format(date)
}