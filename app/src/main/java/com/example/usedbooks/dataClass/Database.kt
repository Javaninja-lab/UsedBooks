package com.example.usedbooks.dataClass

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class Database {
    private var database = Firebase.firestore
    private var storageReference = FirebaseStorage.getInstance().getReference()
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
        return Studente("0",name, surname, email, User("0", "Prova"))
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

   private fun getPhotoMateriale(Uri: String): Bitmap{
       val imagereference = storageReference.child(Uri)
       lateinit var  bitmap: Bitmap
       val localfile : File = File.createTempFile("test","jpg")
       imagereference.getFile(localfile).addOnSuccessListener {
           bitmap= BitmapFactory.decodeFile(localfile.absolutePath)
       }
       return bitmap
   }

    private fun addAnnuncio(materialeDaAggiungere: MaterialeDaAggiungere){
        val id = addMateriale(materialeDaAggiungere.nome,materialeDaAggiungere.descrizione, materialeDaAggiungere.tipologia,materialeDaAggiungere.prezzo,materialeDaAggiungere.latitudine,materialeDaAggiungere.longitudine, "Vendita",materialeDaAggiungere.corso)
        addPhotos(materialeDaAggiungere.photos!!,id)
    }

    private fun addPhotos(photos: ArrayList<Photo>, idMateriale: String){
        photos.forEach{
                photo->
            var uri = Uri.parse(photo.localUri)
            val uriimageRemote="image/materiali/${idMateriale}/${uri.lastPathSegment}"
            val imageRef = storageReference.child(uriimageRemote)
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(ContentValues.TAG, "Image uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                        remoteUri->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabaseMateriale(uriimageRemote,idMateriale)
                }
            }
            uploadTask.addOnFailureListener{
                Log.e(ContentValues.TAG, it.message?: "No message")
            }
        }
    }

    private fun updatePhotoDatabaseMateriale(photo:String, idMateriale: String) {

        var photoCollection = database.collection("materiale").document(idMateriale).collection("photos")
        var handle = photoCollection.add(photo)
        handle.addOnSuccessListener {
            Log.i(ContentValues.TAG, "successfully update photo metadata with"+it.id)

        }
        handle.addOnFailureListener{
            Log.e(ContentValues.TAG, "error updating photo data: ${it.message}")
        }

        /*var photoCollection = database.collection("studenti").document(Database.getLoggedStudent().id).collection("photos")
        var handle = photoCollection.add(photo)
        handle.addOnSuccessListener {
            Log.i(ContentValues.TAG, "successfully update photo metadata")
            photo.id=it.id
            var photoCollection = database.collection("studenti").document(Database.getLoggedStudent().id).collection("photos").document(photo.id).set(photo)
        }
        handle.addOnFailureListener{
            Log.e(ContentValues.TAG, "error updating photo data: ${it.message}")
        }*/
    }


    //return -1 se non trova il corso
    private fun addMateriale(nome : String, descrizione : String, tipologia: String,prezzo : Double,latitudine : Double?,longitudine : Double?,stato : String ,NomeCorso : String ): String{
        val c = GeoPoint(latitudine!!,longitudine!!)
        val corso=getCorsoId(NomeCorso)
        if(corso.equals("-1"))
            return "-1"
        val materiale = hashMapOf(
            "cordinate" to c,
            "descrizione" to descrizione,
            "idCorso" to corso,
            "nome" to nome,
            "prezzo" to prezzo,
            "proprietario" to loggedStudente.utente?.id,
            "stato" to stato,
            "tipologia" to tipologia
        )
        var id=""
        database.collection("materiale").add(materiale).addOnSuccessListener {
                documentReference ->
            id=documentReference.id
            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
        }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
        return id
    }

    private fun getStudenti() : ArrayList<Studente> {
        val list : ArrayList<Studente> = ArrayList()
        val i = database.collection("studenti").get()
        while (!i.isComplete); // questa fa schifo
        val k : MutableList<DocumentSnapshot> = i.result.documents
        for(z in k) {
            val utente = User(z.id, z["password"].toString())
            val studente = Studente(z.id, z["nome"].toString(),z["cognome"].toString(),z["email"].toString(),utente)
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
                val utente = User(z.id, z["password"].toString())
                studente = Studente(z.id, z["nome"].toString(),z["cognome"].toString(),z["email"].toString(),utente)
            }
            studente
        }
    }

    fun setLoggedStudent(studente: Studente) {
        loggedStudente = studente
    }

    fun getLastMessage(studenteRichiesto : User, studenteLoggato : User) : Messaggio {
        return Messaggio()
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

        fun setLoggedStudent(studente: Studente) {
            getIstance().setLoggedStudent(studente)
        }
        fun getLoggedStudent() : Studente {
            return getIstance().loggedStudente
        }

        fun getStudenti() : ArrayList<Studente> {
            return getIstance().getStudenti()
        }
        fun getStudente(email : String) : Studente? {
            return getIstance().getStudente(email)
        }
        fun addStudente(name: String, surname: String, email: String, password: String) : Studente? {
            return getIstance().addStudente(name, surname, email, password)
        }
        fun getLastMessage(studenteRichiesto : User, studenteLoggato : User) : Messaggio {
            return getIstance().getLastMessage(studenteRichiesto, studenteLoggato)
        }

        fun getMateriali() : ArrayList<Materiale> {
            return getIstance().getMateriali()
        }
        fun addMateriale(nome : String, descrizione : String, tipologia: String,prezzo : Double,latitudine : Double,longitudine : Double,stato : String ,NomeCorso : String): String{
            return getIstance().addMateriale(nome, descrizione , tipologia,prezzo ,latitudine ,longitudine ,stato ,NomeCorso )
        }

        fun addAnnuncio(materiale : MaterialeDaAggiungere) {
            getIstance().addAnnuncio(materiale)
        }
        fun getPhotoMateriale(uri : String) : Bitmap {
            return getIstance().getPhotoMateriale(uri)
        }
        fun getMaterialiStudente(username: String) : ArrayList<Materiale?> {
            return getIstance().getMaterialiStudente(username)
        }
    }
}