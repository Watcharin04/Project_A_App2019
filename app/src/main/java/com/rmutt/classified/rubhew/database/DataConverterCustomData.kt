package com.rmutt.classified.rubhew.database

import androidx.room.TypeConverter
import com.rmutt.classified.rubhew.dashboard.CustomData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DataConverterCustomData {

    @TypeConverter
    fun fromCustomDataList(customData: List<CustomData>?): String? {
        if (customData == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<CustomData>>() {

        }.type
        return gson.toJson(customData, type)
    }

    @TypeConverter
    fun toCustomDataList(customDataString: String?): List<CustomData>? {
        if (customDataString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<CustomData>>() {

        }.type
        return gson.fromJson(customDataString, type)
    }
}