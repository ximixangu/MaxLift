package com.maxlift.presentation.ui.common

import android.content.Context
import android.content.Intent

fun navigateToActivity(activityClass: Class<*>, context: Context) {
    val intent = Intent()
    intent.setClass(context, activityClass)
    context.startActivity(intent)
}