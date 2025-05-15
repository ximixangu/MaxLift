package com.maxlift.presentation.ui.view.exercise

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maxlift.domain.model.Exercise
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ExerciseCardItem(exercise: Exercise, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.wrapContentSize(),
        shadowElevation = 5.dp,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        ) {
            Column(
                Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.size(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var title = ""
                    exercise.title?.let {
                        title = "$it - "
                    }
                    Text(
                        text = title + exercise.type,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter, "",
                        Modifier.size(13.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.size(5.dp))
                    Text(
                        text = "${exercise.numberOfRepetitions} x ${exercise.weight.roundToInt()}kg",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Text(
                    text = formatDate(exercise.date),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.size(3.dp))
            }
        }
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    return formatter.format(date)
}