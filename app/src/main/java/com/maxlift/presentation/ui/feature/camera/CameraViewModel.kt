package com.maxlift.presentation.ui.feature.camera

import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.TimeSource

class CameraViewModel: ViewModel() {
    private val _lastTime = MutableLiveData<Int?>()
    val lastTime: LiveData<Int?> = _lastTime

    private val directionThreshold = 20f
    private val framesThreshold = 10

    private val timeSource = TimeSource.Monotonic
    private var initialTime: TimeSource.Monotonic.ValueTimeMark? = null

    private var initialPosition: Float? = null
    private var lastPosition: Float? = null

    private var currentMovement = 0

    private var framesCounter: Int = 0
    private var framesCounterUp: Int = 0

    fun processBoundingBox(boundingBox: RectF) {
        viewModelScope.launch {
            if (initialPosition == null) {
                initialPosition = boundingBox.top
                lastPosition = boundingBox.top
            }


            if (boundingBox.top >= lastPosition!! + currentMovement * -20) {
                framesCounter++
                framesCounterUp = 0
                if (framesCounter > 5) currentMovement = 1
            } else if (boundingBox.top <= lastPosition!!) {
                currentMovement = -1
            }

            lastPosition = boundingBox.top

            if (currentMovement == 1 && framesCounter >= 5) {
                initialTime = timeSource.markNow()
            }

            if (initialTime != null) {
                if (boundingBox.top <= initialPosition!!) {
                    val elapsedTime = timeSource.markNow() - initialTime!!
                    val elapsedToInt = elapsedTime.inWholeMilliseconds.toInt()
                    _lastTime.value = elapsedToInt

                    withContext(Dispatchers.Main) {
                        println("--------------------")
                        println("Elapsed Rep Time: ${elapsedTime.inWholeMilliseconds}")
                        println("--------------------")
                    }
                    lastPosition = initialPosition
                    resetBoundingBoxProcessing()
                    initialPosition = lastPosition
                }
            }
        }
    }

    fun processBoundingBoxPlus(boundingBox: RectF) {
        viewModelScope.launch {
            val currentPosition = boundingBox.top

            if (initialPosition == null) {
                initialPosition = currentPosition
            }

            if (lastPosition == null) {
                lastPosition = currentPosition
                initialTime = timeSource.markNow()
                return@launch
            }

            if (currentMovement != -1 && currentPosition >= lastPosition!! - directionThreshold * currentMovement) {
                framesCounter++
                if (framesCounter >= framesThreshold) {
                    currentMovement = 1
                    initialTime = timeSource.markNow()
                }
            } else if(currentPosition < lastPosition!! - directionThreshold * currentMovement) {
                if (currentMovement == 0) {
                    initialPosition = null
                } else {
                    currentMovement = -1
                    if (currentPosition <= initialPosition!!) {
                        storeElapsedTime()
                        resetBoundingBoxProcessing()
                    }
                }
            }
        }
    }

    private fun storeElapsedTime() {
        val elapsedTime = timeSource.markNow() - initialTime!!
        val elapsedToInt = elapsedTime.inWholeMilliseconds.toInt()
        _lastTime.value = elapsedToInt
    }

    fun resetBoundingBoxProcessing() {
        initialPosition = null
        initialTime = null
        currentMovement = 0
        framesCounter = 0
    }
}