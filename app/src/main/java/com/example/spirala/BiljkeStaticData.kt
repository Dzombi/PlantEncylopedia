package com.example.spirala
    var pocetneBiljke = listOf(
        Biljka(
            naziv = "Bosiljak (Ocimum basilicum)",
            porodica = "Lamiaceae (usnate)",
            medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("Salata od paradajza", "Punjene tikvice"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ),
        Biljka(
            naziv = "Nana (Mentha spicata)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTIVBOLOVA),
            profilOkusa = ProfilOkusaBiljke.MENTA,
            jela = listOf("Jogurt sa voćem", "Gulaš"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
            zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.CRNICA)
        ),
        Biljka(
            naziv = "Kamilica (Matricaria chamomilla)",
            porodica = "Asteraceae (glavočike)",
            medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Čaj od kamilice"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Ružmarin (Rosmarinus officinalis)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPRITISKA),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Pečeno pile", "Grah","Gulaš"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.SLJUNKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Lavanda (Lavandula angustifolia)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Jogurt sa voćem"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Peršun (Petroselinum crispum)",
            porodica = "Apiaceae (štitarkovke)",
            medicinskoUpozorenje = "Osobe alergične na biljke iz porodice štitarkovki trebaju izbjegavati peršun.",
            medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE, MedicinskaKorist.SMIRENJE),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Salata od peršuna", "Supa od povrća"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.TROPSKA),
            zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.ILOVACA)
        ),

        Biljka(
            naziv = "Kopriva (Urtica dioica)",
            porodica = "Urticaceae (koprive)",
            medicinskoUpozorenje = "Kontakt s koprivom može izazvati iritaciju kože kod nekih osoba.",
            medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE, MedicinskaKorist.PODRSKAIMUNITETU),
            profilOkusa = ProfilOkusaBiljke.GORKO,
            jela = listOf("Kopriva s krompirom", "Pire od koprive"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.TROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ),



        Biljka(
            naziv = "Matičnjak (Melissa officinalis)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Osobe alergične na biljke iz porodice metvice trebaju izbjegavati upotrebu matičnjaka.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Čaj od matičnjaka", "Pileća supa"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.ILOVACA, Zemljiste.GLINENO)
        ),

        Biljka(
            naziv = "Đumbir (Zingiber officinale)",
            porodica = "Zingiberaceae (đumbirovke)",
            medicinskoUpozorenje = "Osobe koje uzimaju lijekove za razrjeđivanje krvi trebaju izbjegavati konzumaciju velikih količina đumbira.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTIVBOLOVA, MedicinskaKorist.PODRSKAIMUNITETU),
            profilOkusa = ProfilOkusaBiljke.KORIJENASTO,
            jela = listOf("Čaj od đumbira", "Piletina s đumbirom"),
            klimatskiTipovi = listOf(KlimatskiTip.TROPSKA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.GLINENO)
        ),
        Biljka(
            naziv = "Majčina dušica (Thymus vulgaris)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Osobe koje pate od hipertireoze trebaju izbjegavati majčinu dušicu zbog njenog potencijalnog utjecaja na funkciju štitnjače.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PODRSKAIMUNITETU),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Pileći file s majčinom dušicom", "Pire od krompira s majčinom dušicom"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SREDOZEMNA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        )

    )
    var noveBiljke= mutableListOf<Biljka>()

    fun addBiljke(biljka: Biljka){
        noveBiljke.add(biljka)
    }

    fun getBiljke(): List<Biljka> {
        return pocetneBiljke+noveBiljke
    }

    var filter=true

