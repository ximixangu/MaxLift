package com.maxlift.presentation.ui.activities.menuActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MenuScreen()
        }
    }
}