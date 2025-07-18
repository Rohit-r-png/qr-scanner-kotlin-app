package com.example.qrcodescanner.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.qrcodescanner.database.entities.QrResult

@Dao
interface QrResultDao {

    @Query("select * from QrResult order by time desc")
    fun getAllScannedResult(): List<QrResult>

    @Query("select * from QrResult where favorite = 1")
    fun getAllFavoriteResult(): List<QrResult>

    @Query("delete from QrResult")
    fun deleteAllFavoriteResult(): Int

    @Query("delete from QrResult where favorite = 1")
    fun deleteFavoriteResult()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQrResult(qrResult: QrResult) : Long

    @Query("select * from QrResult where id = :id")
    fun getQrResult(id: Int) : QrResult

    @Query("update QrResult set favorite = 1 where id = :id")
    fun addToFavorite(id : Int) : Int

    @Query("update QrResult set favorite = 0 where id = :id")
    fun removeFromFavorite(id : Int) : Int
}