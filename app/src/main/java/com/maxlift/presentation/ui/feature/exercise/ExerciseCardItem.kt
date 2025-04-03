package com.maxlift.presentation.ui.feature.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxlift.domain.model.Exercise
import com.maxlift.domain.model.Person
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExerciseCardItem(exercise: Exercise, person: Person) {
    val name = person.name.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }

    Surface(
        modifier = Modifier.wrapContentSize(),
        shadowElevation = 10.dp
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(180.dp),
        ) {
            Column(Modifier.fillMaxSize()) {
                MiniPersonItem(name, exercise.date)
            }
        }
    }
}

@Composable
fun MiniPersonItem(name: String, date: Date) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.size(15.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.toString()?.uppercase() ?: "?",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }

        Column(Modifier.padding(10.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = formatDate(date),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
    return formatter.format(date)
}