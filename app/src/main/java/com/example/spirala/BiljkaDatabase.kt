package com.example.spirala

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Biljka::class, BiljkaBitmap::class], version = 1)
@TypeConverters(
    MedicinskaKoristConverter::class,
    ProfilOkusaBiljkeConverter::class,
    StringListConverter::class,
    KlimatskiTipConverter::class,
    ZemljisteConverter::class,
    BitmapConverter::class
)
abstract class BiljkaDatabase : RoomDatabase() {
    abstract fun biljkaDao(): BiljkaDAO
    companion object {
        @Volatile
        private var INSTANCE: BiljkaDatabase? = null

        fun getInstance(context: Context): BiljkaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BiljkaDatabase::class.java,
                "biljke-db"
            ).build()
    }
}
