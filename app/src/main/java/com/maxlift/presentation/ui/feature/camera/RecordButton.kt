package com.maxlift.presentation.ui.feature.camera

import android.content.Context
import android.media.AudioManager
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat

const val size = 80

@Composable
fun RecordButton(onClick: () -> Unit) {
    var isClicked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    val view = LocalView.current

    val scale by animateFloatAsState(
        targetValue = if (isClicked) 0.8f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(size.dp)
            .clickable {
                onClick()
                isClicked = !isClicked
                audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, 0.6f)
                view.performHapticFeedback(HapticFeedbackConstantsCompat.VIRTUAL_KEY)
            },
        contentAlignment = Alignment.Center,
    ) {

        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        )

        Box(
            modifier = Modifier
                .size((size*0.90).dp)
                .clip(CircleShape)
                .background(Color.Black)
        )

        Box(
            modifier = Modifier
                .size((size*0.90).dp)
                .scale(scale)
                .clip(CircleShape)
                .border((size*0.03).dp, Color.Black, CircleShape)
                .background(if(isClicked) Color.Red else MaterialTheme.colorScheme.primaryContainer)
        )
    }
}

@Composable
fun DisabledRecordButton() {
    Box(
        modifier = Modifier
            .size(size.dp),
        contentAlignment = Alignment.Center,
    ) {

        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(0.6f))
        )

        Box(
            modifier = Modifier
                .size((size*0.90).dp)
                .clip(CircleShape)
                .background(Color.Black)
        )

        Box(
            modifier = Modifier
                .size((size*0.90).dp)
                .clip(CircleShape)
                .border((size*0.03).dp, Color.Black, CircleShape)
                .background(Color.Gray.copy (0.6f))
        )
    }
}