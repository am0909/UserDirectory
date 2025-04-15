package com.ameya.livefront.userdirectory.data.local

import androidx.room.TypeConverter
import com.ameya.livefront.userdirectory.data.remote.Location
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromLocation(location: Location): String {
        return Gson().toJson(location)
    }

    @TypeConverter
    fun toLocation(locationString: String): Location {
        return Gson().fromJson(locationString, Location::class.java)
    }
}