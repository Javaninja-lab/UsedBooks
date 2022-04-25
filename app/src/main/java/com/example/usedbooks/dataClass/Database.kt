package com.example.usedbooks.dataClass

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Database {
    private var database = Firebase.firestore
    private lateinit var loggedStudente: Studente

    // TODO: Cambiare con chiamata a database
    private fun getMateriali() : ArrayList<Materiale> {
        val dareturn = ArrayList<Materiale>()
        for(i in 1..4) {
            dareturn.add(Materiale(i, "Prova"+i, "Descrizione_"+i, i.toDouble(), i.toFloat(), i.toFloat(), i, i))
        }
        return dareturn
    }

    private fun addStudente(name: String,surname: String,email: String,password: String){
        val studente = hashMapOf(
            "cognome" to surname,
            "nome" to name,
            "email" to email,
            "password" to password
        )
        database.collection("studenti")
            .add(studente)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    private fun getStudenti() : ArrayList<Studente> {
        val list: ArrayList<Studente> = ArrayList<Studente>()
        val i = database.collection("studenti").get()
        while (!i.isComplete){}// questa fa schifo
        val k: MutableList<DocumentSnapshot> = i.result.documents
        for(z in k)
        {
            //TODO : Implementare query per cercare chat
            val studente = Studente(z.id.toInt(),z["nome"].toString(),z["cognome"].toString(),z["email"].toString(),z["password"].toString(), ArrayList<Chat>())
            list.add(studente)
        }
        //Log.d(TAG, "${k[0]["password"]}")
                /*database.collection("studenti")
            .get().addOnSuccessListener { result ->
                for (document in result) {

                    val studente: Studente = Studente(document.id,document["nome"].toString(),document["cognome"].toString(),document["email"].toString(),document["password"].toString())
                    list.add(studente)
                    Log.d(TAG, "${document.id} => ${document["password"]}")
                }
                Log.d(TAG,"${list[0].cognome.toString()}")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }*/


        return list
    }

    fun setLoggedStudent(studente: Studente) {
        loggedStudente = studente
    }

    companion object {
        private lateinit var istance : Database

        private fun getIstance() : Database {
            if(!this::istance.isInitialized)
                istance = Database()
            return istance
        }

        fun setLoggedStudent(studente: Studente) {
            getIstance().setLoggedStudent(studente)
        }

        fun getLoggedStudent() : Studente {
            return getIstance().loggedStudente
        }

        fun inizializateDatabase() : Boolean {
            getIstance()
            return this::istance.isInitialized
        }

        fun getMateriali() : ArrayList<Materiale> {
            return getIstance().getMateriali()
        }

        fun addStudente(name: String, surname: String, email: String, password: String) {
            getIstance().addStudente(name, surname, email, password)
        }

        fun getStudenti(): ArrayList<Studente> {
            return getIstance().getStudenti()
        }
    }
}