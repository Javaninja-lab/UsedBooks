package com.example.usedbooks.dataClass

import java.io.Serializable

data class Materiale(val id : String, val nome : String, val descrizione : String,
                     val tipologia: String, val prezzo : Double, val latitudine : Double,
                     val longitudine : Double, var stato : String, val idCorso : String,
                     val proprietario : String, val photos : ArrayList<String>) : Serializable
