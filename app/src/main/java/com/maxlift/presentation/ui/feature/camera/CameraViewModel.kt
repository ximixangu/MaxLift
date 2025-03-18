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

    private val timeSource = TimeSource.Monotonic
    private var initialTime: TimeSource.Monotonic.ValueTimeMark? = null

    private var initialPosition: RectF? = null
    private var lastPosition: RectF? = null

    private var counter: Int = 0
    private var wentDown: Boolean = false

    fun processBoundingBox(boundingBox: RectF) {
        viewModelScope.launch {
            if (initialPosition == null) {
                initialPosition = boundingBox
                lastPosition = boundingBox
            }

            if (initialTime == null) {
                if (boundingBox.top >= lastPosition!!.top - 5) {
                    lastPosition = boundingBox
                    counter++
                    if (counter >= 10) wentDown = true
                } else if (wentDown) {
                    initialTime = timeSource.markNow()
                }
            } else {
                if (boundingBox.top <= initialPosition!!.top) {
                    val elapsedTime = timeSource.markNow() - initialTime!!
                    val elapsedToInt = elapsedTime.inWholeMilliseconds.toInt()
                    _lastTime.value = elapsedToInt

                    withContext(Dispatchers.Main) {
                        println("--------------------")
                        println("Elapsed Rep Time: ${elapsedTime.inWholeMilliseconds}")
                        println("--------------------")
                    }
                    lastPosition = initialPosition
                    setupBoundingBoxProcessing()
                    initialPosition = lastPosition
                }
            }
        }
    }

    fun setupBoundingBoxProcessing() {
        initialPosition = null
        initialTime = null
        wentDown = false
        counter = 0
    }
}