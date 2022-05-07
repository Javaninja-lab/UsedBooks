package com.example.usedbooks.dataClass

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Database {
    private var database = Firebase.firestore
    private lateinit var loggedStudente: Studente


    private fun getMateriali() : ArrayList<Materiale> {
        val dareturn = ArrayList<Materiale>()
        val i = database.collection("materiale").get()
        while (!i.isComplete){}// questa fa schifo
        val k: MutableList<DocumentSnapshot> = i.result.documents
        for(z in k)
        {
            val c= z.getGeoPoint("cordinate")
            val materiale : Materiale? =
                c?.let {
                    Materiale(z.id,z["nome"].toString(),z["descrizione"].toString(),z["tipologia"].toString(),z["prezzo"].toString().toDouble(),
                        it.latitude,it.longitude,z["stato"].toString(),z["idCorso"].toString(), z["proprietario"].toString())
                }
            if(materiale!=null)
                dareturn.add(materiale)
        }
        return dareturn
    }

    private fun getMaterialiStudente(username: String): ArrayList<Materiale?>{

        val list: ArrayList<Materiale?> = ArrayList<Materiale?>()
        val i =database.collection("studenti").whereEqualTo("proprietario",username).get()
        while (!i.isComplete){}// questa fa schifo
        val k :MutableList<DocumentSnapshot> = i.result.documents
        if(k.size==0)
            return list
        else
        {
            for(z in k)
            {
                val c= z.getGeoPoint("cordinate")
                val materiale =
                    c?.let {
                        Materiale(z.id,z["nome"].toString(),z["descrizione"].toString(),z["tipologia"].toString(),z["prezzo"].toString().toDouble(),
                            it.latitude,it.longitude,z["stato"].toString(),z["idCorso"].toString(), z["proprietario"].toString())
                    }
                list.add(materiale)

            }
        }
        return list

    }




    private fun addStudente(name: String,surname: String,email: String,password: String) : Studente?{
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
        //TODO("Implementare controllo se l'add è andata correttamente e restituire lo studente appena aggiunto")
        return Studente("0", name, surname, email, password)
    }

    //return -1 se non trova il corso
    private fun getCorsoId(nome: String): String{
        val i=database.collection("Corso").whereEqualTo("nome",nome).get()
        while (!i.isComplete){}// questa fa schifo
        val k :MutableList<DocumentSnapshot> = i.result.documents
        if(k.size==0)
            return "-1"
        else
        {
            return k[0].id
        }
    }

    private fun storeImage(){

    }


    //return -1 se non trova il corso
    private fun addMateriale(nome : String, descrizione : String, tipologia: String,prezzo : Double,latitudine : Double,longitudine : Double,stato : String ,NomeCorso : String): Int{
        val c = GeoPoint(latitudine,longitudine)
        val corso=getCorsoId(NomeCorso)
        if(corso.equals("-1"))
            return -1
        val materiale = hashMapOf(
            "cordinate" to c,
            "descrizione" to descrizione,
            "idCorso" to corso,
            "nome" to nome,
            "prezzo" to prezzo,
            "proprietario" to loggedStudente.id.toString(),
            "stato" to stato,
            "tipologia" to tipologia

        )

        database.collection("materiale").add(materiale).addOnSuccessListener {
                documentReference ->
            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
        }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
        return 1

    }

    private fun getStudenti() : ArrayList<Studente> {
        val list : ArrayList<Studente> = ArrayList()
        val i = database.collection("studenti").get()
        while (!i.isComplete); // questa fa schifo
        val k : MutableList<DocumentSnapshot> = i.result.documents
        for(z in k) {
            val studente = Studente(z.id,z["nome"].toString(),z["cognome"].toString(),z["email"].toString(),z["password"].toString())
            list.add(studente)
        }
        return list
    }

    private fun getStudente(username : String) : Studente? {
        val i = database.collection("studenti").whereEqualTo("email",username).get()
        while (!i.isComplete); // questa fa schifo
        val k : MutableList<DocumentSnapshot> = i.result.documents
        return if(k.size == 0)
            null
        else {
            var studente = Studente()
            for(z in k) {
                studente = Studente(z.id,z["nome"].toString(),z["cognome"].toString(),z["email"].toString(),z["password"].toString())
            }
            studente
        }
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

        fun addStudente(name: String, surname: String, email: String, password: String) : Studente? {
            return getIstance().addStudente(name, surname, email, password)
        }

        fun getStudenti() : ArrayList<Studente> {
            return getIstance().getStudenti()
        }
        fun getStudente(email : String) : Studente? {
            return getIstance().getStudente(email)
        }
        fun addMateriale(nome : String, descrizione : String, tipologia: String,prezzo : Double,latitudine : Double,longitudine : Double,stato : String ,NomeCorso : String): Int{
            return getIstance().addMateriale(nome, descrizione , tipologia,prezzo ,latitudine ,longitudine ,stato ,NomeCorso )
        }
    }
}