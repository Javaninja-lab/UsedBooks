package com.example.usedbooks.dataClass

import android.content.IntentSender
import java.util.*

class Messaggio{
    //val mittente : Studente, val destinatario : Studente, val dataInvio : Date, var letto : Boolean, val messaggio : String

    var message:String?= null
    var senderId:String?= null

    constructor(){}

    constructor(message:String?, senderId: String?){
        this.message=message
        this.senderId=senderId
    }
}
