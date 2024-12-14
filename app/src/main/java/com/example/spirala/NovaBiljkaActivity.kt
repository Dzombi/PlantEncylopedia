package com.example.spirala

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class   NovaBiljkaActivity : AppCompatActivity(),
    AdapterView.OnItemSelectedListener{
    private lateinit var naziv : EditText
    private lateinit var porodica : EditText
    private lateinit var medUpozorenje : EditText
    private lateinit var jelo : EditText
    private lateinit var medKoristLV : ListView
    private lateinit var klimTipLV : ListView
    private lateinit var zemTipLV : ListView
    private lateinit var profOkusaLV : ListView
    private lateinit var jelaLV : ListView
    private lateinit var dodajJelo : Button
    private lateinit var dodajBiljku : Button
    private lateinit var uslikajBiljku : Button
    private lateinit var slika : ImageView
    private lateinit var medAdapter: ArrayAdapter<MedicinskaKorist>
    private lateinit var klimAdapter: ArrayAdapter<KlimatskiTip>
    private lateinit var zemAdapter: ArrayAdapter<Zemljiste>
    private lateinit var okusAdapter: ArrayAdapter<ProfilOkusaBiljke>
    private lateinit var jelaAdapter: ArrayAdapter<String>
    private val jela = arrayListOf<String>()
    private lateinit var izabranoJelo: String
    private var edit=false
    private lateinit var trefle: TrefleDAO
    private lateinit var biljkaDAO: BiljkaDAO
    private var capturedBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nova_biljka_layout)

        trefle = TrefleDAO()
        trefle.setContext(this)

        naziv=findViewById(R.id.nazivET)
        porodica=findViewById(R.id.porodicaET)
        medUpozorenje=findViewById(R.id.medicinskoUpozorenjeET)
        jelo=findViewById(R.id.jeloET)
        medKoristLV=findViewById(R.id.medicinskaKoristLV)
        klimTipLV=findViewById(R.id.klimatskiTipLV)
        zemTipLV=findViewById(R.id.zemljisniTipLV)
        profOkusaLV=findViewById(R.id.profilOkusaLV)
        jelaLV=findViewById(R.id.jelaLV)
        dodajJelo=findViewById(R.id.dodajJeloBtn)
        dodajBiljku=findViewById(R.id.dodajBiljkuBtn)
        uslikajBiljku=findViewById(R.id.uslikajBiljkuBtn)
        slika=findViewById(R.id.slikaIV)

        medAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, MedicinskaKorist.entries )
        medKoristLV.adapter=medAdapter

        klimAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, KlimatskiTip.entries )
        klimTipLV.adapter=klimAdapter

        zemAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, Zemljiste.entries )
        zemTipLV.adapter=zemAdapter

        okusAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, ProfilOkusaBiljke.entries )
        profOkusaLV.adapter=okusAdapter

        jelaAdapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,jela)
        jelaLV.adapter=jelaAdapter
        jelaLV.setOnItemClickListener{parent,view,postion,id ->
            izabranoJelo=jela[postion]
            dodajJelo.setText("Izmijeni jelo")
            jelo.setText(izabranoJelo)
            edit=true
        }

        dodajJelo.setOnClickListener{
            if (jelo.text.length < 3 || jelo.text.length > 20) {
                jelo.setError ( "Tekst mora biti duži od 2 znaka i kraći od 20 znakova")
            }
            else if (jela.any { it.equals(jelo.text.toString().lowercase(), ignoreCase = true) }) {
                jelo.setError("Jelo već postoji u listi")
            } else {
                if (edit == false) {
                    jela.add(jelo.text.toString())
                    jelaAdapter.notifyDataSetChanged()
                    jelo.setText("")
                } else if (edit == true) {
                    jela.remove(izabranoJelo)
                    jela.add(jelo.text.toString())
                    jelaAdapter.notifyDataSetChanged()
                    edit = false
                    jelo.setText("")
                    dodajJelo.setText("Dodaj jelo")
                }
            }
        }

        dodajBiljku.setOnClickListener{
            var valid=true
            if (naziv.text.length < 3 || naziv.text.length > 40) {
                naziv.setError ( "Tekst mora biti duži od 2 znaka i kraći od 40 znakova")
                valid=false
            }
            else if (porodica.text.length < 3 || porodica.text.length > 20) {
                porodica.setError ( "Tekst mora biti duži od 2 znaka i kraći od 20 znakova")
                valid=false
            }
            else if (medUpozorenje.text.length < 3 || medUpozorenje.text.length > 20) {
                medUpozorenje.setError ( "Tekst mora biti duži od 2 znaka i kraći od 20 znakova")
                valid=false
            }
            else if(vratiIzabraneKoristi(medKoristLV)== emptyList<MedicinskaKorist>()){
                Toast.makeText(this,"Nije izabrana medicinska korist",Toast.LENGTH_SHORT).show()
                valid=false
            }
            else if(vratiIzabraneKlime(klimTipLV)== emptyList<KlimatskiTip>()){
                Toast.makeText(this,"Nije izabran klimatski tip",Toast.LENGTH_SHORT).show()
                valid=false
            }
            else if(vratiIzabranaZemljista(zemTipLV)== emptyList<Zemljiste>()){
                Toast.makeText(this,"Nije izabran zemljišni tip",Toast.LENGTH_SHORT).show()
                valid=false
            }
            else if(vratiIzabraniOkus(profOkusaLV)== emptyList<ProfilOkusaBiljke>()){
                Toast.makeText(this,"Nije izabran profil okusa",Toast.LENGTH_SHORT).show()
                valid=false
            }
            else if(jela== emptyList<String>()){
                Toast.makeText(this,"Nije dodano jelo",Toast.LENGTH_SHORT).show()
                valid=false
            }


        if(valid){
            var b=Biljka(
                naziv=naziv.text.toString(),
                porodica=porodica.text.toString(),
                medicinskoUpozorenje =  medUpozorenje.text.toString(),
                medicinskeKoristi =  vratiIzabraneKoristi(medKoristLV),
                profilOkusa =  vratiIzabraniOkus(profOkusaLV)[0],
                jela =  jela,
                klimatskiTipovi =  vratiIzabraneKlime(klimTipLV),
                zemljisniTipovi =  vratiIzabranaZemljista(zemTipLV),
                onlineChecked = false
                )
            lifecycleScope.launch {
                val biljkaSaved = biljkaDAO.saveBiljka(trefle.fixData(b))
                if (biljkaSaved) {
                    val savedBiljka = biljkaDAO.getAllBiljkas().find { it.naziv == b.naziv }
                    if (savedBiljka != null) {
                        val biljkaId = savedBiljka.id
                        capturedBitmap?.let { bitmap ->
                            val cropWidth = 100
                            val cropHeight = 100
                            val croppedBitmap = crop(bitmap, cropWidth, cropHeight)
                            biljkaDAO.addImage(biljkaId, croppedBitmap)
                        }
                    }
                } else {
                    Toast.makeText(this@NovaBiljkaActivity, "Greska pri dodavanju biljke!", Toast.LENGTH_SHORT).show()
                }

                val intent = Intent(this@NovaBiljkaActivity, MainActivity::class.java)
                startActivity(intent)
            }

            }
        }

        uslikajBiljku.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Slika je uslikana uspješno
            val extras = data?.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            slika.setScaleType(ImageView.ScaleType.CENTER_CROP);
            slika.setImageBitmap(imageBitmap)
        }
    }

    fun vratiIzabraneKoristi(listView: ListView): MutableList<MedicinskaKorist> {
        val checkedItemPositions = listView.getCheckedItemPositions()
        val itemCount = listView.count
        val selectedItems: MutableList<MedicinskaKorist> = ArrayList()

        for (i in 0 until itemCount) {
            if (checkedItemPositions[i]) {
                val selectedItem = listView.getItemAtPosition(i) as MedicinskaKorist
                selectedItems.add(selectedItem)
            }
        }

        return selectedItems
    }

    fun vratiIzabraneKlime(listView: ListView): MutableList<KlimatskiTip> {
        val checkedItemPositions = listView.getCheckedItemPositions()
        val itemCount = listView.count
        val selectedItems: MutableList<KlimatskiTip> = ArrayList()

        for (i in 0 until itemCount) {
            if (checkedItemPositions[i]) {
                val selectedItem = listView.getItemAtPosition(i) as KlimatskiTip
                selectedItems.add(selectedItem)
            }
        }
        return selectedItems
    }

    fun vratiIzabranaZemljista(listView: ListView): MutableList<Zemljiste> {
        val checkedItemPositions = listView.getCheckedItemPositions()
        val itemCount = listView.count
        val selectedItems: MutableList<Zemljiste> = ArrayList()

        for (i in 0 until itemCount) {
            if (checkedItemPositions[i]) {
                val selectedItem = listView.getItemAtPosition(i) as Zemljiste
                selectedItems.add(selectedItem)
            }
        }
        return selectedItems
    }

    fun vratiIzabraniOkus(listView: ListView): MutableList<ProfilOkusaBiljke> {
        val checkedItemPositions = listView.getCheckedItemPositions()
        val itemCount = listView.count
        val selectedItems: MutableList<ProfilOkusaBiljke> = ArrayList()

        for (i in 0 until itemCount) {
            if (checkedItemPositions[i]) {
                val selectedItem = listView.getItemAtPosition(i) as ProfilOkusaBiljke
                selectedItems.add(selectedItem)
            }
        }
        return selectedItems
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val izbor = parent?.getItemAtPosition(position)
        if(view==jelaLV){

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun crop(bitmap: Bitmap, cropWidth: Int, cropHeight: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val startX = (originalWidth - cropWidth) / 2
        val startY = (originalHeight - cropHeight) / 2
        return Bitmap.createBitmap(bitmap, startX, startY, cropWidth, cropHeight)
    }


}