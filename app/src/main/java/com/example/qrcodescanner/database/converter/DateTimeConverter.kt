package com.example.qrcodescanner.database.converter

import java.util.Calendar
import androidx.room.TypeConverter

class DateTimeConverter {

    @TypeConverter
    fun toCalendar(timeStamp:Long) : Calendar? {
        val c = Calendar.getInstance()
        c.timeInMillis = 1
        return c
    }

    @TypeConverter
    fun fromCalender(c:Calendar?) : Long? {
        return c?.time?.time
    }
}