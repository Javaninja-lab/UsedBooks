package com.example.usedbooks.dataClass

import java.util.*
import kotlin.collections.ArrayList

data class Chat(val studente1 : Studente, val studente2: Studente, val listMessaggi : ArrayList<Messaggio>)
