package com.maxlift.presentation.ui.feature.calculator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreen() {
    val randomRepetitions = listOf(
        130f,
        120f,
        119f,
        125f,
        115f,
        110f,
        109f,
        90f,
        80f,
        50f
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomBarChart(randomRepetitions)
    }
}

@Composable
fun CustomBarChart(
    values: List<Float>
) {
    val bars = ArrayList<Bar>()

    for(value in values) {
        bars.add(Bar(value, Color.Green))
    }

    val colors = generateBarColors(values)
    val maxValue = bars.maxOf { it.value }
    val barWidth = 300f / bars.size
    val barPadding = 30f

    Box(
        modifier = Modifier
            .size(300.dp, 200.dp)
            .border(width = 1.dp, color = Color(0xFFA7FF8C))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            val referenceLineCount = 5
            val step = size.height / referenceLineCount

            for (i in 1..referenceLineCount) {
                val y = size.height - (i * step)
                drawLine(
                    color = Color.Gray,
                    strokeWidth = 1f,
                    start = Offset(0f, y),
                    end = Offset(size.width, y)
                )
            }

            drawLine(
                color = Color.Black,
                strokeWidth = 2f,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height)
            ) // X Axis

            drawLine(
                color = Color.Black,
                strokeWidth = 2f,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height)
            ) // Y Axis

            bars.forEachIndexed { index, bar ->
                val left = index * (barWidth + barPadding) + barPadding
                val top = size.height - (bar.value / maxValue) * size.height

                drawRect(
                    color = colors[index],
                    topLeft = Offset(left, top),
                    size = Size(barWidth, size.height - top)
                )
            }
        }
    }
}

data class Bar(val value: Float, val color: Color)

fun generateBarColors(values: List<Float>): List<Color> {
    val colors = mutableListOf<Color>()
    var previousValue: Float = Float.MIN_VALUE

    values.forEach { currentValue ->
        val currentColor = if (currentValue >= previousValue) {
            Color.Green
        } else {
            blendColors(Color.Green, Color.Red, weight = (previousValue - currentValue) * 3 / previousValue)
        }

        colors.add(currentColor)
        previousValue = currentValue
    }

    return colors
}

fun blendColors(color1: Color, color2: Color, weight: Float): Color {
    val clampedWeight = weight.coerceIn(0f, 1f)
    return Color(
        red = (color1.red * (1 - clampedWeight) + color2.red * clampedWeight),
        green = (color1.green * (1 - clampedWeight) + color2.green * clampedWeight),
        blue = (color1.blue * (1 - clampedWeight) + color2.blue * clampedWeight),
        alpha = 1f
    )
}