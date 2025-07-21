package com.example.qrcodescanner.database

import androidx.room.Query
import com.example.qrcodescanner.database.db.QrResultDatabase
import com.example.qrcodescanner.database.entities.QrResult
import java.util.Calendar

class DBHelperI(var qrResultDatabase: QrResultDatabase) : DBHelper {
    override fun insertQrResult(result: String) : Int {
        val time = Calendar.getInstance()
        val resultType = "TEXT"
        val qrResult = QrResult(result = result, resultType = resultType, calendar = time, favorite = false)
        return qrResultDatabase.getQrDao().insertQrResult(qrResult).toInt()
    }

    override fun getQrResult(id: Int): QrResult {
        return qrResultDatabase.getQrDao().getQrResult(id)
    }

    override fun addToFavorite(id: Int): Int {
        return qrResultDatabase.getQrDao().addToFavorite(id)
    }

    override fun removeFromFavorite(id: Int): Int {
        return qrResultDatabase.getQrDao().removeFromFavorite(id)
    }

    override fun getAllQrScannedResult(): List<QrResult> {
        return qrResultDatabase.getQrDao().getAllScannedResult()
    }

    override fun getAllQrFavaroiteResult(): List<QrResult> {
        return qrResultDatabase.getQrDao().getAllFavoriteResult()
    }

    override fun deleteQrResult(id: Int): Int {
        return qrResultDatabase.getQrDao().deleteQrResult(id)
    }

    override fun deleteAllQrScannedResult() {
        qrResultDatabase.getQrDao().deleteAllScannedResult()
    }

    override fun deleteAllFavQrScanResults() {
        qrResultDatabase.getQrDao().deleteAllFavoriteResult()
    }

}