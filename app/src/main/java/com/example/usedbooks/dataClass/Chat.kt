package com.example.usedbooks.dataClass

import java.util.*
import kotlin.collections.ArrayList

data class Chat(val studente1 : Studente, val studente2: Studente, val listMessaggi : ArrayList<Messaggio>)
data class Messaggio(val mittente : Studente, val destinatario : Studente, val dataInvio : Date, var letto : Boolean, val messaggio : String)
