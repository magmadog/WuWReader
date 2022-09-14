package com.sarbaevartur.wuwreader.db

import androidx.room.TypeConverter
import java.util.*

class BookConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}