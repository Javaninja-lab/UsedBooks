package com.example.usedbooks.dataClass

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Database {
    private val database = Firebase.firestore

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

    fun getStudenti() : MutableList<Studente> {
        val list: MutableList<Studente> = mutableListOf()
        database.collection("studenti")
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    val studente = document.toObject(Studente::class.java)
                    list.add(studente)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return list
    }

    companion object {
        private lateinit var istance : Database

        private fun getIstance() : Database {
            if(!this::istance.isInitialized)
                istance = Database()
            return istance
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

        fun getStudenti(): MutableList<Studente>{
            return getIstance().getStudenti()
        }
    }
}