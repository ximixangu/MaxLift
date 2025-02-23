package com.maxlift.data.datasource

import android.content.Context
import com.maxlift.R
import java.io.IOException

class AssetsProvider {
    companion object{
        fun getJsonDataFromRawAsset(context: Context): String? {
            val jsonString: String
            try {
                jsonString = context.resources.openRawResource(
                    R.raw.user).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }
    }
}