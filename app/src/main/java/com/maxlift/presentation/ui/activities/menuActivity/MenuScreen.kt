package com.maxlift.presentation.ui.activities.menuActivity

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxlift.presentation.ui.activities.cameraActivity.CameraActivity
import com.maxlift.presentation.ui.activities.loginActivity.LoginActivity

@Composable
fun MenuScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navigateToActivity(CameraActivity::class.java, context) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(BorderStroke(3.dp, color = Color.White)),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffbf87c0))
        ) {
            Text(text = "Camera", fontSize = 32.sp)
        }

        Button(
            onClick = { navigateToActivity(LoginActivity::class.java, context) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(BorderStroke(3.dp, color = Color.White))
            ,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffbf87f0))
        ) {
            Text(text = "User Login", fontSize = 32.sp)
        }

        Button(
            onClick = { navigateToActivity(MenuActivity::class.java, context) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(BorderStroke(3.dp, color = Color.White))
            ,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffb8b7c0))
        ) {
            Text(text = "Activity 3", fontSize = 32.sp)
        }
    }
}

private fun navigateToActivity(activityClass: Class<*>, context: Context) {
    val intent = Intent()
    intent.setClass(context, activityClass)
    context.startActivity(intent)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMenuScreen() {
    MenuScreen()
}
