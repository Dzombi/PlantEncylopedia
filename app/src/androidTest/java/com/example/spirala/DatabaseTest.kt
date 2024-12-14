package com.example.spirala

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var biljkaDao: BiljkaDAO
    private lateinit var db: BiljkaDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BiljkaDatabase::class.java).build()
        biljkaDao = db.biljkaDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun Test1() = runBlocking {
        val biljka = Biljka(
            naziv = "Nana (Mentha spicata)",
            porodica = "Test",
            medicinskoUpozorenje = "",
            medicinskeKoristi = emptyList(),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("Test"),
            klimatskiTipovi = emptyList(),
            zemljisniTipovi = emptyList(),
            onlineChecked = false
        )

        biljkaDao.saveBiljka(biljka)
        val allBiljkas = biljkaDao.getAllBiljkas()
        assertThat(allBiljkas.size, equalTo(1))
        assertThat(allBiljkas[0], equalTo(biljka.copy(id = allBiljkas[0].id)))
    }

    @Test
    @Throws(Exception::class)
    fun Test2() = runBlocking {
        val biljka1 = Biljka(
            naziv = "Nana (Mentha spicata)",
            porodica = "Test",
            medicinskoUpozorenje = "",
            medicinskeKoristi = emptyList(),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("Test"),
            klimatskiTipovi = emptyList(),
            zemljisniTipovi = emptyList(),
            onlineChecked = false
        )
        val biljka2 = Biljka(
            naziv = "Kamilica (Matricaria chamomilla)",
            porodica = "Test",
            medicinskoUpozorenje = "",
            medicinskeKoristi = emptyList(),
            profilOkusa = ProfilOkusaBiljke.GORKO,
            jela = listOf("Test"),
            klimatskiTipovi = emptyList(),
            zemljisniTipovi = emptyList(),
            onlineChecked = false
        )
        biljkaDao.saveBiljka(biljka1)
        biljkaDao.saveBiljka(biljka2)
        biljkaDao.clearData()
        val allBiljkas = biljkaDao.getAllBiljkas()
        assertThat(allBiljkas.size, equalTo(0))
    }
}
