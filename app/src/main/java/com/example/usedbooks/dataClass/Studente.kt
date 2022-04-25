package com.example.usedbooks.dataClass

data class Studente(val id : Int, val nome : String, val cognome : String, val email : String, val password: String, var listChat: ArrayList<Chat>)
