package com.maxlift.presentation.ui.view.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.maxlift.presentation.ui.view.person.list.PersonViewModel

@Composable
fun SelectPersonPopUp(
    personViewModel: PersonViewModel,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val personList by personViewModel.personListState.observeAsState(null)
    var rows = 1

    LaunchedEffect(Unit) {
        personViewModel.fetchAllPersons()
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Select Person",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                if (personList != null) {
                    if (personList!!.size >= 6) {
                        rows = 2
                    }
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(rows),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.fillMaxWidth().height(140.dp * rows)
                    ) {
                        items(personList!!.size) { index ->
                            val person = personList!![index]
                            PersonCardItem(person) {
                                onSelect(person.id.toString())
                                onDismiss()
                            }
                        }
                    }
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }

                    Spacer(Modifier.size(10.dp))
                }
            }
        }
    }
}