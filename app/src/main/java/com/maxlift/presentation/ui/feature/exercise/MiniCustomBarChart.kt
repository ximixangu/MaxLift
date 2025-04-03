package com.maxlift.presentation.ui.feature.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MiniBarChart(
    values: List<Int>,
    modifier: Modifier = Modifier
) {
    val maxValue = values.maxOrNull() ?: 1
    val barWidth = 10.dp

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        values.forEach { value ->
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .fillMaxHeight(value.toFloat() / maxValue)
                    .background(Color.Blue, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}
