package com.example.spirala

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),
    AdapterView.OnItemSelectedListener,
    MedicinskiAdapter.MedicinskiEvent ,
    KuharskiAdapter.KuharskiEvent,
    BotanickiAdapter.BotanickiEvent{
    private lateinit var button: Button
    private lateinit var dodaj: Button
    private lateinit var biljkeRV: RecyclerView
    private var filteredBiljkeData: ArrayList<Biljka> = arrayListOf()
    private lateinit var medicinskiAdapter: MedicinskiAdapter
    private lateinit var kuharskiAdapter: KuharskiAdapter
    private lateinit var botanickiAdapter: BotanickiAdapter
    private lateinit var spinner: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<CharSequence>
    private lateinit var trefle: TrefleDAO
    private lateinit var trazi: Button
    private lateinit var pretraga:EditText
    private lateinit var boja: Spinner
    private lateinit var bojaAdapter: ArrayAdapter<CharSequence>
    private lateinit var biljkaDAO : BiljkaDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trefle = TrefleDAO()
        trefle.setContext(this)

        biljkaDAO = BiljkaDatabase.getInstance(this).biljkaDao()
        val db = BiljkaDatabase.getInstance(this)
        biljkaDAO = db.biljkaDao()

        filteredBiljkeData.addAll(getBiljke())

        spinner = findViewById(R.id.modSpinner)
        spinner.onItemSelectedListener = this
        spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        boja = findViewById(R.id.bojaSPIN)
        bojaAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.boja_spinner_items,
            android.R.layout.simple_spinner_item
        )
        bojaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        boja.adapter = bojaAdapter

        biljkeRV = findViewById(R.id.biljkeRV)
        biljkeRV.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )


        button = findViewById(R.id.resetBtn)


        dodaj = findViewById(R.id.novaBiljkaBtn)


        trazi = findViewById(R.id.brzaPretraga)


        pretraga = findViewById(R.id.pretragaET)

        lifecycleScope.launch {
            database()
            var biljke = biljkaDAO.getAllBiljkas()


            kuharskiAdapter = KuharskiAdapter(biljke, this@MainActivity, trefle, biljkaDAO)
            botanickiAdapter = BotanickiAdapter(biljke, this@MainActivity, trefle, biljkaDAO)
            medicinskiAdapter = MedicinskiAdapter(biljke, this@MainActivity, trefle, biljkaDAO)
            biljkeRV.adapter = medicinskiAdapter



        fun onClickReset(v: View?) {
            filter = true
            filteredBiljkeData = arrayListOf()
            filteredBiljkeData.addAll(biljke)
            val trenutni = biljkeRV.adapter
            if (trenutni == medicinskiAdapter) {
                medicinskiAdapter = MedicinskiAdapter(filteredBiljkeData, this@MainActivity, trefle,biljkaDAO)
                biljkeRV.adapter = medicinskiAdapter
            } else if (trenutni == kuharskiAdapter) {
                kuharskiAdapter = KuharskiAdapter(filteredBiljkeData, this@MainActivity, trefle,biljkaDAO)
                biljkeRV.adapter = kuharskiAdapter
            } else if (trenutni == botanickiAdapter) {
                botanickiAdapter = BotanickiAdapter(filteredBiljkeData, this@MainActivity, trefle,biljkaDAO)
                biljkeRV.adapter = botanickiAdapter
            }
        }

        fun onClickAdd(v: View?) {
            val intent = Intent(this@MainActivity, NovaBiljkaActivity::class.java)
            startActivity(intent)
        }

        fun onClickTrazi(v: View?) {
            val query = pretraga.text.toString()
            val selectedColor = boja.selectedItem.toString()
            if (query.isNotEmpty() && selectedColor != "Pick a color") {
                var t = this@MainActivity
                lifecycleScope.launch {
                    val searchResults = trefle.getPlantsWithFlowerColor(selectedColor, query)
                    filter = false
                    botanickiAdapter = BotanickiAdapter(searchResults, t, trefle, biljkaDAO)
                    biljkeRV.adapter = botanickiAdapter
                }
            } else {
                Toast.makeText(this@MainActivity, "Choose a color and write a query", Toast.LENGTH_SHORT).show()
            }
        }

            button.setOnClickListener {
                onClickReset(button)
            }
            dodaj.setOnClickListener {
                onClickAdd(dodaj)
            }
            trazi.setOnClickListener {
                onClickTrazi(trazi)
            }

        }
    }

    override fun onMedicinskiItemClick(position: Int) {
        lifecycleScope.launch {
            database()
            var biljka = biljkaDAO.getAllBiljkas()[position]
            filteredBiljkeData = arrayListOf()

            for (i in 0..<getBiljke().size) {
                var tf = false

                for (j in 0..<biljka.medicinskeKoristi.size) {
                    for (z in 0..<getBiljke()[i].medicinskeKoristi.size) {
                        if (biljka.medicinskeKoristi[j] == getBiljke()[i].medicinskeKoristi[z]) {
                            tf = true
                            break
                        }
                    }
                    if (tf) break
                }

                if (tf) {
                    filteredBiljkeData.add(getBiljke()[i])
                }
            }
            medicinskiAdapter = MedicinskiAdapter(filteredBiljkeData, this@MainActivity, trefle, biljkaDAO)
            biljkeRV.adapter = medicinskiAdapter
        }
    }

    override fun onKuharskiItemClick(position: Int) {
        lifecycleScope.launch {
            database()
            var biljka = biljkaDAO.getAllBiljkas()[position]
            filteredBiljkeData = arrayListOf()

            for (i in 0..<getBiljke().size) {
                var tf = false

                if (biljka.profilOkusa == getBiljke()[i].profilOkusa) {
                    tf = true
                }

                for (j in 0..<biljka.jela.size) {
                    if (tf) break
                    for (z in 0..<getBiljke()[i].jela.size) {
                        if (biljka.jela[j] == getBiljke()[i].jela[z]) {
                            tf = true
                            break
                        }
                    }
                    if (tf) break
                }

                if (tf) {
                    filteredBiljkeData.add(getBiljke()[i])
                }
            }
            kuharskiAdapter = KuharskiAdapter(filteredBiljkeData, this@MainActivity, trefle, biljkaDAO)
            biljkeRV.adapter = kuharskiAdapter
        }
    }

    override fun onBotanickiItemClick(position: Int) {
        if (!filter) return
        lifecycleScope.launch {
            database()
            var biljka = biljkaDAO.getAllBiljkas()[position]
            filteredBiljkeData = arrayListOf()

            for (i in 0..<getBiljke().size) {
                var klim = false
                var zem = false

                if (biljka.porodica == getBiljke()[i].porodica) {
                    for (j in 0..<biljka.klimatskiTipovi.size) {
                        for (z in 0..<getBiljke()[i].klimatskiTipovi.size) {
                            if (biljka.klimatskiTipovi[j] == getBiljke()[i].klimatskiTipovi[z]) {
                                klim = true
                                break
                            }
                        }
                        if (klim) break
                    }

                    for (j in 0..<biljka.zemljisniTipovi.size) {
                        for (z in 0..<getBiljke()[i].zemljisniTipovi.size) {
                            if (biljka.zemljisniTipovi[j] == getBiljke()[i].zemljisniTipovi[z]) {
                                zem = true
                                break
                            }
                        }
                        if (zem) break
                    }
                }

                if (klim && zem) {
                    filteredBiljkeData.add(getBiljke()[i])
                }
            }
            botanickiAdapter = BotanickiAdapter(filteredBiljkeData, this@MainActivity, trefle, biljkaDAO)
            biljkeRV.adapter = botanickiAdapter
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        filter = true
        val izbor = parent?.getItemAtPosition(position)
        if (izbor.toString() == "Medicinski") {
            sakrij()
            medicinskiAdapter = MedicinskiAdapter(filteredBiljkeData, this, trefle,biljkaDAO)
            biljkeRV.adapter = medicinskiAdapter
        } else if (izbor.toString() == "Kuharski") {
            sakrij()
            kuharskiAdapter = KuharskiAdapter(filteredBiljkeData, this, trefle,biljkaDAO)
            biljkeRV.adapter = kuharskiAdapter
        } else if (izbor.toString() == "Botanički") {
            prikazi()
            botanickiAdapter = BotanickiAdapter(filteredBiljkeData, this, trefle,biljkaDAO)
            biljkeRV.adapter = botanickiAdapter
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }



        private fun prikazi() {
            boja.visibility = View.VISIBLE
            pretraga.visibility = View.VISIBLE
            trazi.visibility = View.VISIBLE
        }

        private fun sakrij() {
            boja.visibility = View.GONE
            pretraga.visibility = View.GONE
            trazi.visibility = View.GONE
        }
        private suspend fun database() {
            val biljkeDB = biljkaDAO.getAllBiljkas()
            if (biljkeDB.isEmpty()) {
                getBiljke().forEach { biljka ->
                    biljkaDAO.saveBiljka(biljka)
                }
            }
        }

}