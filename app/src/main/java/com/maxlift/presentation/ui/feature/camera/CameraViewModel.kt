package com.maxlift.presentation.ui.feature.camera

import android.content.Context
import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxlift.domain.model.Exercise
import com.maxlift.domain.usecase.exercise.SaveExerciseUseCase
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
    private var repDistance = mutableListOf<Float>()
    private var lowestPosition:  Float = Float.MIN_VALUE
    private var highestPosition: Float = Float.MAX_VALUE

    private var directionThreshold: Float? = null
    private var currentMovement = 0
    private var framesNotMoving = 0

    fun processBoundingBoxPlus(boundingBox: RectF) {
        viewModelScope.launch {
            val currentPosition = boundingBox.top
            directionThreshold = boundingBox.height() * 0.01f

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
                framesNotMoving = 0
            } else if (currentPosition < lastPosition!! - directionThreshold!!) {
                if (currentMovement == 0) initialTime = timeSource.markNow()
                currentMovement = -1
                lastPosition = currentPosition
                framesNotMoving = 0
            } else {
                framesNotMoving++
            }

            when(currentMovement) {
                1 -> {
                    if (initialTime != null && previousMovement == -1) {
                        storeElapsedTime()
                        stopTime = null
                        highestPosition = Float.MAX_VALUE
                    }
                    if (currentPosition > lowestPosition) {
                        lowestPosition = currentPosition
                    }
                }
                -1 -> {
                    if (previousMovement == 1) {
                        initialTime = timeSource.markNow()
                    }

                    if(currentPosition < highestPosition) {
                        stopTime = timeSource.markNow()
                        highestPosition = currentPosition
                    }

                    if (framesNotMoving > 5 && initialTime != null) {
                        storeElapsedTime()
                        stopTime = null
                        initialTime = null
                        highestPosition = Float.MAX_VALUE
                    }
                }
            }
        }
    }

    private fun storeElapsedTime() {
        val elapsedTime: Duration =
            if(stopTime != null) stopTime!! - initialTime!!
            else timeSource.markNow() - initialTime!!

        if (elapsedTime.inWholeMilliseconds.toInt() > 200)
            addTime(elapsedTime.inWholeMilliseconds.toInt())
    }

    private fun addTime(time: Int){
        if(_times.value == null) _times.value = mutableListOf()
        _times.value?.add(time)
        repDistance.add(lowestPosition - highestPosition)
        highestPosition = Float.MAX_VALUE
        lowestPosition = Float.MIN_VALUE
    }

    fun setExerciseTitle(title: String) {
        _exercise.value?.title = title
    }

    fun setExerciseDescription(description: String) {
        _exercise.value?.description = description
    }

    fun resetBoundingBoxProcessing() {
        _times.value = null
        repDistance = mutableListOf()
        initialPosition = null
        initialTime = null
        currentMovement = 0
    }

    fun stopProcessing() {
        viewModelScope.launch {
            val maxDistance = repDistance.max()
            val timesList = mutableListOf<Int>()
            println(repDistance)
            repDistance.forEachIndexed { i, distance ->
                println(distance / maxDistance)
                if (distance / maxDistance >= 0.7) {
                    _times.value?.let {
                        println(it.size)
                        timesList.add(it[i])
                    }
                }
            }
            _times.value = timesList
        }
    }

    fun saveCurrentExercise(context: Context) {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

            _exercise.value?.personId = sharedPreferences.getInt("person", 1)
            _exercise.value?.type = sharedPreferences.getString("type", "No Type")!!
            _exercise.value?.weight = sharedPreferences.getInt("weight", 50).toFloat()
            _exercise.value?.times = _times.value!!.map { it.toFloat() }
            _exercise.value?.numberOfRepetitions = _times.value!!.size

            try {
                _exercise.value?.let {
                    SaveExerciseUseCase.execute(context, it)
                }
            } catch (e: Exception) {
                println("Error saving current exercise: ${e.message}")
            }
        }
    }
}