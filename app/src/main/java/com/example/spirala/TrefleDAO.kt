package com.example.spirala

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class TrefleDAO {
    private val token = "1u5eBIZ_Ukz8cy8ICydofSiRDjX9OyRX4uB_QeIdxkI"

    private lateinit var context: Context
    fun setContext(newContext: Context) {
        context = newContext
    }

    companion object {
        private var defaultBitmap: Bitmap? = null

        fun getDefaultBitmap(context: Context): Bitmap {
            if (defaultBitmap == null) {
                defaultBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default1)
            }
            return defaultBitmap!!
        }


    }

    suspend fun getImage(biljka: Biljka): Bitmap? {
        return withContext(Dispatchers.IO) {
                val latinskoIme = biljka.naziv.substringAfter("(").substringBefore(")")
                val response = RetrofitInstance.api.searchBiljke(latinskoIme, token)
                if (response.isSuccessful) {
                    val biljke = response.body()?.data
                    if (!biljke.isNullOrEmpty()) {
                        val imageUrl = biljke[0].imageUrl
                        BitmapFactory.decodeStream(URL(imageUrl).openConnection().getInputStream())
                    } else {
                        getDefaultBitmap(context)
                    }
                } else {
                    getDefaultBitmap(context)
                }
        }
    }

    suspend fun fixData(biljka: Biljka): Biljka {
        return withContext(Dispatchers.IO) {
            val latinskoIme = biljka.naziv.substringAfter("(").substringBefore(")")
            val searchResponse = RetrofitInstance.api.searchBiljke(latinskoIme, token)
            if (searchResponse.isSuccessful) {
                val searchResults = searchResponse.body()?.data
                if (!searchResults.isNullOrEmpty()) {
                    val id = searchResults[0].id
                    val detailsResponse = RetrofitInstance.api.getBiljkeDetails(id, token)
                    if (detailsResponse.isSuccessful) {
                        val details = detailsResponse.body()?.data
                        if (details != null) {

                            var medicinskoUpozorenje = details.mainSpecies?.specfications?.toxicity?.takeIf { it != "none" }?.let {
                                    val upozorenje = "TOKSIČNO"
                                    if (biljka.medicinskoUpozorenje.contains(upozorenje)) {
                                        biljka.medicinskoUpozorenje
                                    } else {
                                        biljka.medicinskoUpozorenje+" "+upozorenje.trim()
                                    }
                                } ?: biljka.medicinskoUpozorenje

                            var jela: List<String> = biljka.jela
                            if (details.mainSpecies?.edible == false) {
                                medicinskoUpozorenje += if (medicinskoUpozorenje.isEmpty()) "NIJE JESTIVO" else " NIJE JESTIVO"
                                jela = listOf()
                            }

                            val zemljisniTipovi = details.mainSpecies?.growth?.soilTexture?.mapNotNull { soil ->
                                when (soil.toIntOrNull()) {
                                    10 -> Zemljiste.KRECNJACKO
                                    9 -> Zemljiste.SLJUNKOVITO
                                    7, 8 -> Zemljiste.CRNICA
                                    5, 6 -> Zemljiste.ILOVACA
                                    3, 4 -> Zemljiste.PJESKOVITO
                                    1, 2 -> Zemljiste.GLINENO
                                    else -> null
                                }
                            } ?: emptyList()

                            val klimatskiTipovi = mutableListOf<KlimatskiTip>()
                            val atmospheric_humidity = details.mainSpecies?.growth?.atmosphericHumidity
                            val light = details.mainSpecies?.growth?.light
                            if (light != null && atmospheric_humidity != null) {
                                if (light in 6..9 && atmospheric_humidity in 1..5) klimatskiTipovi.add(KlimatskiTip.SREDOZEMNA)
                                if (light in 8..10 && atmospheric_humidity in 7..10) klimatskiTipovi.add(KlimatskiTip.TROPSKA)
                                if (light in 6..9 && atmospheric_humidity in 5..8) klimatskiTipovi.add(KlimatskiTip.SUBTROPSKA)
                                if (light in 4..7 && atmospheric_humidity in 3..7) klimatskiTipovi.add(KlimatskiTip.UMJERENA)
                                if (light in 7..9 && atmospheric_humidity in 1..2) klimatskiTipovi.add(KlimatskiTip.SUHA)
                                if (light in 0..5 && atmospheric_humidity in 3..7) klimatskiTipovi.add(KlimatskiTip.PLANINSKA)
                            }

                            return@withContext biljka.copy(
                                porodica = details.family.name,
                                medicinskoUpozorenje = medicinskoUpozorenje,
                                klimatskiTipovi = klimatskiTipovi,
                                zemljisniTipovi = zemljisniTipovi,
                                jela = jela
                            )

                        } else {
                            throw Exception("No details")
                        }
                    } else {
                        throw Exception(detailsResponse.errorBody()?.string())
                    }
                } else {
                    throw Exception("No results")
                }
            } else {
                throw Exception(searchResponse.errorBody()?.string())
            }
        }
    }


    suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {
        return withContext(Dispatchers.IO) {
            val biljkaList = mutableListOf<Biljka>()
            var page = 1
            var totalPages = 1
            do {
                val response = RetrofitInstance.api.getBiljkeColor(flowerColor.lowercase(), substr, token, page)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val biljke = body.data
                        if (biljke != null) {
                            for (biljka in biljke) {
                                val detailsResponse = RetrofitInstance.api.getBiljkeDetails(biljka.id, token)
                                if (detailsResponse.isSuccessful) {
                                    val details = detailsResponse.body()?.data
                                    if (details != null) {

                                        var ime: String = details?.scientificName?: "???"
                                        var porodica: String = details?.family?.name ?: ""

                                        val zemljisniTipovi = details.mainSpecies?.growth?.soilTexture?.mapNotNull { soil ->
                                            when (soil.toIntOrNull()) {
                                                10 -> Zemljiste.KRECNJACKO
                                                9 -> Zemljiste.SLJUNKOVITO
                                                7, 8 -> Zemljiste.CRNICA
                                                5, 6 -> Zemljiste.ILOVACA
                                                3, 4 -> Zemljiste.PJESKOVITO
                                                1, 2 -> Zemljiste.GLINENO
                                                else -> null
                                            }
                                        } ?: emptyList()

                                        val klimatskiTipovi = mutableListOf<KlimatskiTip>()
                                        val light = details?.mainSpecies?.growth?.light
                                        val atmospheric_humidity = details?.mainSpecies?.growth?.atmosphericHumidity
                                        if (light != null && atmospheric_humidity != null) {
                                            if (light in 6..9 && atmospheric_humidity in 1..5) klimatskiTipovi.add(KlimatskiTip.SREDOZEMNA)
                                            if (light in 8..10 && atmospheric_humidity in 7..10) klimatskiTipovi.add(KlimatskiTip.TROPSKA)
                                            if (light in 6..9 && atmospheric_humidity in 5..8) klimatskiTipovi.add(KlimatskiTip.SUBTROPSKA)
                                            if (light in 4..7 && atmospheric_humidity in 3..7) klimatskiTipovi.add(KlimatskiTip.UMJERENA)
                                            if (light in 7..9 && atmospheric_humidity in 1..2) klimatskiTipovi.add(KlimatskiTip.SUHA)
                                            if (light in 0..5 && atmospheric_humidity in 3..7) klimatskiTipovi.add(KlimatskiTip.PLANINSKA)
                                        }

                                        biljkaList.add( Biljka(
                                            naziv = ime,
                                            porodica = porodica,
                                            medicinskoUpozorenje = "",
                                            medicinskeKoristi = listOf(),
                                            profilOkusa = null,
                                            jela = listOf(),
                                            klimatskiTipovi = klimatskiTipovi,
                                            zemljisniTipovi = zemljisniTipovi
                                            )
                                        )

                                    }
                                }
                            }
                        }
                        totalPages = (body.meta.total / 20) + 1
                        page++
                    } else {
                        break
                    }
                } else {
                    break
                }
            } while (page <= totalPages)
            return@withContext biljkaList
        }
    }


}