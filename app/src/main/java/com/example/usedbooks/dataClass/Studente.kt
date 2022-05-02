package com.example.usedbooks.dataClass

data class Studente(val id : String, val nome : String, val cognome : String, val email : String, val password: String) {
    constructor() : this("","","","","")
}
