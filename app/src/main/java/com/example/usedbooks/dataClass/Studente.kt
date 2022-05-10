package com.example.usedbooks.dataClass

data class Studente(val nome : String, val cognome : String, val email : String, val utente : User?) {
    constructor() : this("","","",null)
}
