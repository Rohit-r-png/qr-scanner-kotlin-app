package com.example.qrcodescanner.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.qrcodescanner.database.converter.DateTimeConverter
import java.util.Calendar

@Entity
@TypeConverters(DateTimeConverter::class)
data class QrResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "result")
    val result: String?,

    @ColumnInfo(name = "result_type")
    val resultType: String?,

    @ColumnInfo(name = "favorite")
    val favorite: Boolean?,

    @ColumnInfo(name = "time")
    val calendar: Calendar
)
