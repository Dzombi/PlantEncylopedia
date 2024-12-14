package com.example.spirala

import androidx.room.*
import android.graphics.Bitmap

@Dao
interface BiljkaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBiljka(biljka: Biljka): Long
    @Transaction
    suspend fun saveBiljka(biljka: Biljka): Boolean {
        val result = insertBiljka(biljka)
        return result != -1L
    }

    @Query("SELECT * FROM biljka WHERE onlineChecked = 0")
    suspend fun getOfflineBiljke(): List<Biljka>

    @Update
    suspend fun updateBiljka(biljka: Biljka): Int

    @Transaction
    suspend fun fixOfflineBiljka(): Int {
        val offlineBiljke = getOfflineBiljke()
        var updatedCount = 0
        for (biljka in offlineBiljke) {
            val fixedBiljka = TrefleDAO().fixData(biljka)
            if (biljka != fixedBiljka) {
                updateBiljka(fixedBiljka.copy(onlineChecked = true))
                updatedCount++
            }
        }
        return updatedCount
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(biljkaBitmap: BiljkaBitmap): Long
    @Transaction
    suspend fun addImage(idBiljke: Long, bitmap: Bitmap): Boolean {
        val biljka = getAllBiljkas().find { it.id == idBiljke }
        return if (biljka != null && getBitmapById(idBiljke) == null) {
            val biljkaBitmap = BiljkaBitmap(idBiljke = idBiljke, bitmap = bitmap)
            insertImage(biljkaBitmap)
            true
        } else {
            false
        }
    }

    @Transaction
    suspend fun clearData() {
        clearBiljkas()
        clearBiljkaBitmaps()
    }

    @Query("SELECT * FROM biljka")
    suspend fun getAllBiljkas(): List<Biljka>

    @Query("DELETE FROM biljka")
    suspend fun clearBiljkas()

    @Query("DELETE FROM BiljkaBitmap")
    suspend fun clearBiljkaBitmaps()

    @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :biljkaId")
    suspend fun getBitmapById(biljkaId: Long): BiljkaBitmap?
}
