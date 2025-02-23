package com.maxlift.presentation.ui.common

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun BackButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Button(
        onClick = {
            if (context is Activity) {
                context.finish()
            }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIosNew,
            contentDescription = "Go Back Icon"
        )
    }
}
