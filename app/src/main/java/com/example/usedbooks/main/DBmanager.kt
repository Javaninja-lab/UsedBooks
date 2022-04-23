package com.example.usedbooks.main

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.usedbooks.dataClass.Studente
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DBmanager {

    val db = Firebase.firestore

    fun AddStudente(name: String,surname: String,email: String,password: String,){
        val studente = hashMapOf(
            "cognome" to surname,
            "nome" to name,
            "email" to email,
            "password" to password
        )
        db.collection("studenti")
            .add(studente)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }


    /*.
         }*/
    fun getStudenti() : MutableList<Studente> {
        val list: MutableList<Studente> = mutableListOf()
        db.collection("studenti")
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

}