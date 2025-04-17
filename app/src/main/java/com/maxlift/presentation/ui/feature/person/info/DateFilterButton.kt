package com.maxlift.presentation.ui.feature.person.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maxlift.presentation.ui.feature.exercise.formatDate
import java.util.Date

@Composable
fun DateRangeButton(
    onDateRangeSelected: (Long?, Long?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var start by remember { mutableStateOf<Long?>(null) }
    var end by remember { mutableStateOf<Long?>(null) }

    Surface(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(0.4f)),
        color = Color.White,
        shadowElevation = 0.dp,
        onClick = { showDialog = true }
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (start == null && end == null) {
                    "Date"
                } else if (start == null) {
                    "${formatDate(Date(end!!))} or earlier"
                } else if (end == null) {
                    "${formatDate(Date(start!!))} or later"
                } else {
                    "${formatDate(Date(start!!))} to ${formatDate(Date(end!!))}"
                },
                style = MaterialTheme.typography.labelSmall,
            )

            DateRangePickerDialog(
                showDialog,
                onDismiss = { showDialog = false }
            ) { startDate, endDate ->
                start = startDate
                end = endDate
                onDateRangeSelected(startDate, endDate)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDateRangeSelected: (Long?, Long?) -> Unit
) {
    if (showDialog) {
        val dateRangePickerState = rememberDateRangePickerState()
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateRangeSelected(
                        dateRangePickerState.selectedStartDateMillis,
                        dateRangePickerState.selectedEndDateMillis
                    )
                    onDismiss()
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Cancel") }
            },
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                title = { Text("Select Date Range") },
                headline = {
                    var text = "Start Date - End Date"
                    dateRangePickerState.selectedStartDateMillis?.let {
                        text = text.replace("Start Date", formatDate(Date(it)))
                    }
                    dateRangePickerState.selectedEndDateMillis?.let {
                        text = text.replace("End Date", formatDate(Date(it)))
                    }
                    Text(text)
                },
                showModeToggle = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(16.dp)
            )
        }
    }
}