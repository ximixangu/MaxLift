package com.maxlift.presentation.ui.feature.camera

import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.TimeSource

class CameraViewModel: ViewModel() {
    private val _lastTime = MutableLiveData<Int?>()
    val lastTime: LiveData<Int?> = _lastTime

    private val timeSource = TimeSource.Monotonic
    private var initialTime: TimeSource.Monotonic.ValueTimeMark? = null
    private var stopTime: TimeSource.Monotonic.ValueTimeMark? = null

    private var initialPosition: Float? = null
    private var lastPosition: Float? = null
    private var highestPosition: Float? = Float.MAX_VALUE

    private var directionThreshold: Float? = null
    private var currentMovement = 0

    fun processBoundingBoxPlus(boundingBox: RectF) {
        viewModelScope.launch {
            val currentPosition = boundingBox.top
            directionThreshold = boundingBox.height() * 0.05f

            if (initialPosition == null) {
                initialPosition = currentPosition
            }

            if (lastPosition == null) {
                lastPosition = currentPosition
                initialTime = timeSource.markNow()
                return@launch
            }

            val previousMovement = currentMovement
            if(currentPosition > lastPosition!! + directionThreshold!!) {
                if (currentMovement == 0) initialTime = null
                currentMovement = 1
                lastPosition = currentPosition
            } else if (currentPosition < lastPosition!! - directionThreshold!!) {
                if (currentMovement == 0) initialTime = timeSource.markNow()
                currentMovement = -1
                lastPosition = currentPosition
            }

            when(currentMovement) {
                1 -> {
                    if (initialTime != null && previousMovement == -1) {
                        storeElapsedTime()
                        stopTime = null
                        highestPosition = Float.MAX_VALUE
                    }
                }
                -1 -> {
                    if (previousMovement == 1) {
                        initialTime = timeSource.markNow()
                    }

                    if(currentPosition < highestPosition!!) {
                        stopTime = timeSource.markNow()
                        highestPosition = currentPosition
                    }
                }
            }
        }
    }

    private fun storeElapsedTime() {
        val elapsedTime: Duration = stopTime!! - initialTime!!
        _lastTime.value = elapsedTime.inWholeMilliseconds.toInt()
    }

    fun resetBoundingBoxProcessing() {
        initialPosition = null
        initialTime = null
        currentMovement = 0
    }
}