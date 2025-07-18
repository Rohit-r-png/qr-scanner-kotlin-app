package com.example.qrcodescanner.database

import com.example.qrcodescanner.database.entities.QrResult

interface DBHelper {
    fun insertQrResult(result: String) : Int
    fun getQrResult(id : Int) : QrResult
    fun addToFavorite(id:Int) : Int
    fun removeFromFavorite(id: Int) : Int
    fun getAllQrScannedResult() : List<QrResult>
    fun getAllQrFavaroiteResult() : List<QrResult>
}