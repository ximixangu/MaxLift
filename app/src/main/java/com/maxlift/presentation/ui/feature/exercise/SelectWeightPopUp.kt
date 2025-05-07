package com.maxlift.presentation.ui.feature.exercise

import android.content.Context
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.maxlift.presentation.ui.common.EditableTextField

@Composable
fun SelectWeightPopUp(
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val weight = sharedPreferences.getInt("weight", 50)
    var currentWeight by remember { mutableStateOf(weight.toString()) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp),
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Select Weight",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.size(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfiniteCircularList(
                        width = 60.dp,
                        itemHeight = 35.dp,
                        items = (1..250).toList(),
                        initialItem = currentWeight.toIntOrNull() ?: 1,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        textColor = MaterialTheme.colorScheme.secondary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        onItemSelected = { _, item ->
                            onSelect(item.toString())
                        }
                    )
                    Text(
                        text = "kg",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Spacer(Modifier.size(5.dp))

                EditableTextField(
                    value = currentWeight,
                    onValueChange = {
                        currentWeight = it
                        if (currentWeight.isBlank()) onSelect("1")
                        else onSelect(currentWeight)
                    },
                    label = "Weight",
                    keyboardType = KeyboardType.Number
                )

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
fun <T> InfiniteCircularList(
    width: Dp,
    itemHeight: Dp,
    numberOfDisplayedItems: Int = 3,
    items: List<T>,
    initialItem: T,
    itemScaleFact: Float = 1.5f,
    textStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    onItemSelected: (index: Int, item: T) -> Unit = { _, _ -> }
) {
    val itemHalfHeight = LocalDensity.current.run { itemHeight.toPx() / 2f }
    val scrollState = rememberLazyListState(0)
    var lastSelectedIndex by remember {
        mutableIntStateOf(0)
    }
    var itemsState by remember {
        mutableStateOf(items)
    }
    LaunchedEffect(items) {
        var targetIndex = items.indexOf(initialItem) - 1
        targetIndex += ((Int.MAX_VALUE / 2) / items.size) * items.size
        itemsState = items
        lastSelectedIndex = targetIndex
        scrollState.scrollToItem(targetIndex)
    }
    LazyColumn(
        modifier = Modifier
            .width(width)
            .height(itemHeight * numberOfDisplayedItems),
        state = scrollState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = scrollState
        )
    ) {
        items(
            count = Int.MAX_VALUE,
            itemContent = { i ->
                val item = itemsState[i % itemsState.size]
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            val y = coordinates.positionInParent().y - itemHalfHeight
                            val parentHalfHeight = (coordinates.parentCoordinates?.size?.height ?: 0) / 2f
                            val isSelected = (y > parentHalfHeight - itemHalfHeight && y < parentHalfHeight + itemHalfHeight)
                            if (isSelected && lastSelectedIndex != i) {
                                onItemSelected(i % itemsState.size, item)
                                lastSelectedIndex = i
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.toString(),
                        style = textStyle,
                        color = if (lastSelectedIndex == i) {
                            selectedTextColor
                        } else {
                            textColor
                        },
                        fontSize = if (lastSelectedIndex == i) {
                            textStyle.fontSize * itemScaleFact
                        } else {
                            textStyle.fontSize
                        }
                    )
                }
            }
        )
    }
}