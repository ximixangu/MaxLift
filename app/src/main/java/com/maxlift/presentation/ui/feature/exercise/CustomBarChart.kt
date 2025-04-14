package com.maxlift.presentation.ui.feature.exercise

import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs


@Composable
fun CustomBarChart(
    values: List<Int>
) {
    val colors = generateBarColors(values)
    val maxValue = values.max()
    val barWidth = minOf(275.dp / values.size, 70.dp)
    val barPadding = minOf(25.dp / values.size)
    val rectBounds = mutableListOf<Rect>()
    var pressedBarIndex by remember { mutableStateOf<Int?>(null) }
    var textOffset by remember { mutableStateOf(IntOffset.Zero) }
    var lineHeight by remember { mutableIntStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Time (ms)",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.rotate(270f).width(1.dp).height(50.dp).offset((-25).dp),
            maxLines = 1,
            overflow = TextOverflow.Visible,
            softWrap = false,
        )

        Box(modifier = Modifier.size(300.dp, 200.dp)) {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp)
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val position = event.changes.firstOrNull()?.position ?: continue

                                rectBounds.forEachIndexed { index, rect ->
                                    if (position.x >= rect.left && position.x <= rect.right) {
                                        pressedBarIndex = index
                                        textOffset =
                                            IntOffset(
                                                x = (rect.left - 40.dp.toPx()).toInt() + rect.width() / 2,
                                                y = (-10.dp.toPx()).toInt()
                                            )
                                        lineHeight = rect.top
                                    }
                                }

                                if (rectBounds.none { rect ->
                                        position.x >= rect.left && position.x <= rect.right
                                    }) {
                                    pressedBarIndex = null
                                }
                            }
                        }
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

                rectBounds.clear()

                values.forEachIndexed { index, value ->
                    val left = index * (barWidth + barPadding).toPx() + 1
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
                    .width(80.dp),
                    contentAlignment = Alignment.TopCenter
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = values[pressedBarIndex!!].toString() + " ms",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium,
                            overflow = TextOverflow.Visible,
                            maxLines = 1,
                            softWrap = false
                        )

                        Canvas(modifier = Modifier.fillMaxHeight()) {
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, 0f),
                                end = Offset(0f, lineHeight.toFloat()),
                                strokeWidth = 2f,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(20f, 10f),
                                    0f
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

fun generateBarColors(values: List<Int>): List<Color> {
    val colors = mutableListOf<Color>()
    var previousValue: Int = Int.MIN_VALUE

    values.forEach { currentValue ->
        val currentColor =
            blendColors(
                Color.Green,
                Color.Red,
                weight = abs(previousValue - currentValue).toFloat() / previousValue
            )
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