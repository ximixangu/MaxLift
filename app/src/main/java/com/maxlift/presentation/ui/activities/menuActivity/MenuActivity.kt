package com.maxlift.presentation.ui.activities.menuActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.maxlift.presentation.theme.MaxLiftTheme

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaxLiftTheme {
                Surface {
                    MenuScreen()
                }
            }
        }
    }
}