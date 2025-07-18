package com.example.qrcodescanner.utils

import android.view.View
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun View.visible(){
    this.visibility = View.VISIBLE
}
fun View.inVisible(){
    this.visibility = View.INVISIBLE
}
fun View.gone(){
    this.visibility = View.GONE
}

fun Calendar.toFormatedDisplay(): String {
    val simpleDateFormat = SimpleDateFormat("dd-mm-yy hh:mm:a", Locale.US)
    return simpleDateFormat.format(this.time)
}