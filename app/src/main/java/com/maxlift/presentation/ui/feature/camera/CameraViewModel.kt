package com.maxlift.presentation.ui.feature.camera

import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxlift.domain.model.Exercise
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.TimeSource

class CameraViewModel: ViewModel() {
    private val _times = MutableLiveData<MutableList<Int>?>()
    val times: LiveData<MutableList<Int>?> = _times

    private val _exercise = MutableLiveData(Exercise())
    val exercise: LiveData<Exercise> = _exercise

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
        val elapsedTime: Duration =
            if(stopTime != null) stopTime!! - initialTime!!
            else timeSource.markNow() - initialTime!!

        if (elapsedTime.inWholeMilliseconds.toInt() > 0)
            addTime(elapsedTime.inWholeMilliseconds.toInt())
    }

    private fun addTime(time: Int){
        if(_times.value == null) _times.value = mutableListOf()
        _times.value?.add(time)
    }

    fun setExerciseTitle(title: String) {
        _exercise.value?.title = title
    }

    fun setExerciseDescription(description: String) {
        _exercise.value?.description = description
    }

    fun resetBoundingBoxProcessing() {
        _times.value = null
        initialPosition = null
        initialTime = null
        currentMovement = 0
    }
}