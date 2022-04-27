package com.example.usedbooks.dataClass

import java.util.*

data class Messaggio(val mittente : Studente, val destinatario : Studente, val dataInvio : Date, var letto : Boolean, val messaggio : String)
