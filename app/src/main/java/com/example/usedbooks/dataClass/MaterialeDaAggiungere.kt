package com.example.usedbooks.dataClass

import android.graphics.Bitmap
import java.io.Serializable

data class MaterialeDaAggiungere(val nome : String, val descrizione : String, val tipologia: String,
                                 val prezzo : Double, val latitudine : Double?,
                                 val longitudine : Double?, val immagine : Bitmap?) : Serializable{
    constructor(nome: String, descrizione: String, tipologia: String, prezzo: Double)
            : this(nome, descrizione, tipologia, prezzo, null, null, null)

    constructor(materialeDaAggiungere: MaterialeDaAggiungere, latitudine: Double, longitudine: Double)
            : this(materialeDaAggiungere.nome, materialeDaAggiungere.descrizione,
        materialeDaAggiungere.tipologia, materialeDaAggiungere.prezzo, latitudine, longitudine, null)
}