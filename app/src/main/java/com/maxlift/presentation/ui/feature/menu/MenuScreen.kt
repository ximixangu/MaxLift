package com.maxlift.presentation.ui.feature.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController: NavController) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if(lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED)
                    navController.navigate("camera")
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(BorderStroke(3.dp, color = Color.White)),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffbf87c0))
        ) {
            Text(text = "TFLite", fontSize = 32.sp)
        }

        Button(
            onClick = {
                if(lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED)
                    navController.navigate("mlkit")
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(BorderStroke(3.dp, color = Color.White))
            ,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffbf87f0))
        ) {
            Text(text = "MLKit", fontSize = 32.sp)
        }

        Button(
            onClick = {
                if(lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED)
                    navController.navigate("persons")
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(BorderStroke(3.dp, color = Color.White)),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffb8b7c0))
        ) {
            Text(text = "People", fontSize = 32.sp)
        }
    }
}