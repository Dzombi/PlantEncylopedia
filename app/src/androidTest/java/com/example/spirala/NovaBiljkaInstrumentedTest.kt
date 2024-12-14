package com.example.spirala

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.hamcrest.number.OrderingComparison.greaterThan
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestS2 {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        NovaBiljkaActivity::class.java
    )
    @Test
    fun testValidacijeNaziva(){
        onView(withId(R.id.nazivET)).perform(typeText("a"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.nazivET)).perform(scrollTo())
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova")))

        onView(withId(R.id.nazivET)).perform(clearText(), typeText("aaaaaaaaaaaaaaaaaaaaa"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.nazivET)).perform(scrollTo())
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova")))

        onView(withId(R.id.nazivET)).perform(clearText(), typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.nazivET)).perform(scrollTo())
        onView(withId(R.id.nazivET)).check(matches(not(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova"))))

    }

    fun testValidacijePorodice(){
        onView(withId(R.id.porodicaET)).perform(typeText("a"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.porodicaET)).perform(scrollTo())
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova")))

        onView(withId(R.id.porodicaET)).perform(clearText(), typeText("aaaaaaaaaaaaaaaaaaaaa"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.porodicaET)).perform(scrollTo())
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova")))

        onView(withId(R.id.porodicaET)).perform(clearText(), typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.porodicaET)).perform(scrollTo())
        onView(withId(R.id.porodicaET)).check(matches(not(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova"))))

    }

    fun testValidacijeMedicinskogUpozorenja(){
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("a"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova")))

        onView(withId(R.id.medicinskoUpozorenjeET)).perform(clearText(), typeText("aaaaaaaaaaaaaaaaaaaaaa"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova")))

        onView(withId(R.id.medicinskoUpozorenjeET)).perform(clearText(), typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(not(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova"))))

    }

    private lateinit var mockImageBitmap : Bitmap
    private fun createMockImageBitmap(): Bitmap {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return BitmapFactory.decodeResource(context.resources, R.drawable.default1)
    }

    @Test
    fun testSlike(){
        onView(withId(R.id.uslikajBiljkuBtn)).perform(scrollTo())
        mockImageBitmap=createMockImageBitmap()
        Intents.init()
        val resultIntent = Intent()
        resultIntent.putExtra("data", mockImageBitmap)
        val result = Activity.RESULT_OK
        Intents.intending(allOf(
            hasAction(MediaStore.ACTION_IMAGE_CAPTURE)
        )).respondWith(
            Instrumentation.ActivityResult(result, resultIntent)
        )
        onView(withId(R.id.uslikajBiljkuBtn)).perform(click())
        onView(withId(R.id.slikaIV)).perform(scrollTo()).check {view,_->
            val imageView = view as ImageView
            val displayedBitmap = (imageView.drawable as BitmapDrawable).bitmap
            assertEquals(mockImageBitmap.width, displayedBitmap.width)
            assertEquals(mockImageBitmap.height, displayedBitmap.height)

            for (x in 0 until mockImageBitmap.width) {
                for (y in 0 until mockImageBitmap.height) {
                    assertEquals(
                        "Pixel mismatch at x=$x, y=$y",
                        mockImageBitmap.getPixel(x, y),
                        displayedBitmap.getPixel(x, y)
                    )
                }
            }
        }
        Intents.release()
    }

    @Test
    fun testValidacijaMedicinskeKoristi(){
        onView(withId(R.id.nazivET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(scrollTo())
        onView(withId(R.id.porodicaET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withText("Nije izabrana medicinska korist"))
            .inRoot(ToastMatcher().apply {
                (matches(isDisplayed()))
            })
    }

    @Test
    fun testValidacijaKlimatskogTipa(){
        onView(withId(R.id.nazivET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(scrollTo())
        onView(withId(R.id.porodicaET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo(), click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withText("Nije izabran klimatski tip"))
            .inRoot(ToastMatcher().apply {
                (matches(isDisplayed()))
            })
    }

    @Test
    fun testValidacijaZemljisnogTipa(){
        onView(withId(R.id.nazivET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(scrollTo())
        onView(withId(R.id.porodicaET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo(), click())
        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo(), click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withText("Nije izabran zemljišni tip"))
            .inRoot(ToastMatcher().apply {
                (matches(isDisplayed()))
            })
    }

    @Test
    fun testValidacijaProfilaOkusa(){
        onView(withId(R.id.nazivET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(scrollTo())
        onView(withId(R.id.porodicaET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo(), click())
        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo(), click())
        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo(), click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withText("Nije izabran profil okusa"))
            .inRoot(ToastMatcher().apply {
                (matches(isDisplayed()))
            })
    }

    @Test
    fun testValidacijaJela(){
        onView(withId(R.id.nazivET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(scrollTo())
        onView(withId(R.id.porodicaET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo(), click())
        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo(), click())
        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo(), click())
        onView(withId(R.id.profilOkusaLV)).perform(scrollTo(), click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withText("Nije dodano jelo"))
            .inRoot(ToastMatcher().apply {
                (matches(isDisplayed()))
            })
    }

    @Test
    fun testDodavanjeJela() {
        onView(withId(R.id.jeloET)).perform(typeText("a"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
        onView(withId(R.id.jeloET)).perform(scrollTo())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova")))

        onView(withId(R.id.jeloET)).perform(clearText(), typeText("aaaaaaaaaaaaaaaaaaaaaa"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
        onView(withId(R.id.jeloET)).perform(scrollTo())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova")))

        onView(withId(R.id.jeloET)).perform(clearText(), typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
        onView(withId(R.id.jeloET)).perform(scrollTo())
        onView(withId(R.id.jeloET)).check(matches(not(hasErrorText("Tekst mora biti duži od 2 znaka i kraći od 20 znakova"))))
        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
        onView(withId(R.id.jelaLV)).check(matches(hasDescendant(withText("aaa"))))

        onView(withId(R.id.jeloET)).perform(clearText(), typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
        onView(withId(R.id.jeloET)).perform(scrollTo())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Jelo već postoji u listi")))


    }

    }

