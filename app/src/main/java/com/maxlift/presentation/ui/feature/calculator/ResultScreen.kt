package com.maxlift.presentation.ui.feature.calculator

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.maxlift.presentation.ui.feature.camera.CameraViewModel
import kotlin.math.abs

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
                modifier = Modifier.padding(vertical = 30.dp)
            )

            CustomBarChart(it)

            Text(
                "# of repetitions: ${it.size}" +
                    "\nAverage time: ${it.average().toInt()} ms",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        }
    }
}

@Composable
fun CustomBarChart(
    values: List<Int>
) {
    val colors = generateBarColors(values)
    val maxValue = values.max()
    val barWidth = minOf(270.dp / values.size)
    val barPadding = minOf(30.dp / values.size)
    val rectBounds = mutableListOf<Rect>()
    var pressedBarIndex by remember { mutableStateOf<Int?>(null) }
    var textOffset by remember { mutableStateOf(IntOffset.Zero) }


    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Time(ms)",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.rotate(270f)
        )

        Box(
            modifier = Modifier
                .size(300.dp, 200.dp)
                // .border(width = 1.dp, color = Color(0xFFA7FF8C))
        ) {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { offset ->
                                rectBounds.forEachIndexed { index, rect ->
                                    if (offset.x >= rect.left && offset.x <= rect.right) {
                                        pressedBarIndex = index
                                        textOffset = IntOffset(rect.left, rect.top - 30)

                                        try {
                                            awaitRelease()
                                        } finally {
                                            pressedBarIndex = null
                                        }
                                    }
                                }
                            }
                        )
                    }
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

                val initialPadding = minOf(barPadding)
                rectBounds.clear()

                values.forEachIndexed { index, value ->
                    val left = index * (barWidth + barPadding).toPx() + initialPadding.toPx()
                    val top = size.height - (value.toFloat() / maxValue) * size.height

                    drawRect(
                        color = colors[index],
                        topLeft = Offset(left, top),
                        size = Size(barWidth.toPx(), size.height - top)
                    )

                    rectBounds.add(
                        Rect(
                            left.toInt(),
                            top.toInt(),
                            (left + barWidth.toPx()).toInt(),
                            (size.height).toInt() + 20
                        )
                    )
                }
            }

            if (pressedBarIndex != null) {
                Box(modifier = Modifier
                        .offset { textOffset }
                        .width(barWidth),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = values[pressedBarIndex!!].toString() + " ms",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Visible,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}

fun generateBarColors(values: List<Int>): List<Color> {
    val colors = mutableListOf<Color>()
    var previousValue: Int = Int.MIN_VALUE

    values.forEach { currentValue ->
        val currentColor = if (currentValue <= previousValue) {
            Color.Green
        } else {
            blendColors(
                Color.Green,
                Color.Red,
                weight = abs(previousValue - currentValue).toFloat() / previousValue
            )
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