package com.maxlift.presentation.ui.feature.camera

import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxlift.domain.model.Measurement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.TimeSource

class CameraViewModel: ViewModel() {
    private val _measureState = MutableLiveData<Measurement?>()
    val measurement: LiveData<Measurement?> get() = _measureState

    private val timeSource = TimeSource.Monotonic
    private var initialTime: TimeSource.Monotonic.ValueTimeMark? = null

    private var initialPosition: RectF? = null
    private var lastPosition: RectF? = null

    suspend fun processBoundingBox(boundingBox: RectF) {
        if(initialPosition == null) {
            initialPosition = boundingBox
            lastPosition = boundingBox
        }

        if(initialTime == null) {
            if(boundingBox.top >= lastPosition!!.top - 5) {
                lastPosition = boundingBox
            }else{
                initialTime = timeSource.markNow()
            }
        }else {
            if(boundingBox.top <= initialPosition!!.top) {
                val elapsedTime = timeSource.markNow() - initialTime!!
                withContext(Dispatchers.Main) {
                    println("--------------------")
                    println("Elapsed Rep Time: ${elapsedTime.inWholeMilliseconds}")
                    println("--------------------")
                }
                initialPosition = null
                initialTime = null
            }
        }
    }
}