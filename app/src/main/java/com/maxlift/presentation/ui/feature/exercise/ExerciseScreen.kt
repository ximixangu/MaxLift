package com.maxlift.presentation.ui.feature.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maxlift.presentation.ui.feature.camera.CameraViewModel

@Composable
fun ResultScreen(viewModel: CameraViewModel) {
    val times by viewModel.times.observeAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        times?.let {
            Text(
                "Sample Exercise",
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                "24/03/2024",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            CustomBarChart(it)

            Text(
                "# of repetitions: ${it.size}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                "Average time: ${it.average().toInt()} ms",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}