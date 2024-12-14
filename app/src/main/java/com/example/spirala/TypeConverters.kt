package com.example.spirala

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class MedicinskaKoristConverter {
    @TypeConverter
    fun fromList(value: List<MedicinskaKorist>): String {
        return value.joinToString(",") { it.opis }
    }

    @TypeConverter
    fun toList(value: String): List<MedicinskaKorist> {
        return value.split(",").mapNotNull { opis ->
            MedicinskaKorist.values().find { it.opis == opis }
        }
    }
}

class ZemljisteConverter {
    @TypeConverter
    fun fromList(value: List<Zemljiste>): String {
        return value.joinToString(",") { it.naziv }
    }

    @TypeConverter
    fun toList(value: String): List<Zemljiste> {
        return value.split(",").mapNotNull { naziv ->
            Zemljiste.entries.find { it.naziv == naziv }
        }
    }
}

class KlimatskiTipConverter {
    @TypeConverter
    fun fromList(value: List<KlimatskiTip>): String {
        return value.joinToString(",") { it.opis }
    }

    @TypeConverter
    fun toList(value: String): List<KlimatskiTip> {
        return value.split(",").mapNotNull { opis ->
            KlimatskiTip.entries.find { it.opis == opis }
        }
    }
}

class ProfilOkusaBiljkeConverter {
    @TypeConverter
    fun fromProfilOkusa(profilOkusa: ProfilOkusaBiljke?): String? {
        return profilOkusa?.opis
    }

    @TypeConverter
    fun toProfilOkusa(value: String?): ProfilOkusaBiljke? {
        return value?.let { opis ->
            ProfilOkusaBiljke.entries.find { it.opis == opis }
        }
    }
}

class StringListConverter {
    @TypeConverter
    fun fromList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return value.split(",")
    }
}

class BitmapConverter {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @TypeConverter
    fun toBitmap(base64String: String): Bitmap {
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
