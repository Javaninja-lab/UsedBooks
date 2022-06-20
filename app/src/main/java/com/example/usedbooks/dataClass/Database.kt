package com.example.usedbooks.dataClass

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Database {
    private var database = Firebase.firestore
    private var storageReference = FirebaseStorage.getInstance().reference
    private lateinit var loggedStudente: Studente
    private var mDbref= FirebaseDatabase.getInstance().reference

    private fun getMateriali() : ArrayList<Materiale> {
        val dareturn = ArrayList<Materiale>()
        val i = database.collection("materiale").get()
        while (!i.isComplete);
        val k: MutableList<DocumentSnapshot> = i.result.documents
        for(z in k)
        {
            val  uri: ArrayList<String> = ArrayList()
            uri.add(getUriPhotosMateriale(z.id))
            val c= z.getGeoPoint("cordinate")
            val materiale : Materiale? =
                c?.let {
                    Materiale(z.id,z["nome"].toString(),z["descrizione"].toString(),z["tipologia"].toString(),z["prezzo"].toString().toDouble(),
                        it.latitude,it.longitude,z["stato"].toString(),z["idCorso"].toString(), z["proprietario"].toString(),uri)
                }
            if(materiale!=null && !materiale.stato.equals("venduto"))
                dareturn.add(materiale)
        }
        return dareturn
    }

    private fun getUriPhotosMateriale(idMateriale: String) : String{
        val x=  database.collection("materiale/$idMateriale/photos").get();
        while(!x.isComplete);
        val y: MutableList<DocumentSnapshot> = x.result.documents;
        val string= y[0]["remoteUri"].toString()
        return string;
    }

    private fun getUriPhotosStudente(idStudente: String) : String{
        val x = database.collection("studenti/$idStudente/photos").get();
        while(!x.isComplete);
        val y: MutableList<DocumentSnapshot> = x.result.documents
        return if(y.size>0){
            val string= y[0]["remoteUri"].toString()
            string
        } else
            ""
    }

    private fun deleteLastUriPhotosStudente(idstudente: String){
        val x=  database.collection("studenti/$idstudente/photos").get();
        while(!x.isComplete);
        val y: MutableList<DocumentSnapshot> = x.result.documents;
        if(y.size>0) {
            val idLastPhoto = y[0].id
            database.collection("studenti/$idstudente/photos").document(idLastPhoto).delete()
        }

    }

    private fun getMaterialiStudente(username: String, checkVendita : Boolean): ArrayList<Materiale?>{

        val list: ArrayList<Materiale?> = ArrayList<Materiale?>()
        val i =database.collection("materiale").whereEqualTo("proprietario",username).get()
        while (!i.isComplete);
        val k :MutableList<DocumentSnapshot> = i.result.documents
        if(k.size==0)
            return list
        else
        {
            for(z in k)
            {
                val  uri: ArrayList<String> = ArrayList()
                uri.add(getUriPhotosMateriale(z.id))
                val c= z.getGeoPoint("cordinate")
                val materiale =
                    c?.let {
                        Materiale(z.id,z["nome"].toString(),z["descrizione"].toString(),z["tipologia"].toString(),z["prezzo"].toString().toDouble(),
                            it.latitude,it.longitude,z["stato"].toString(),z["idCorso"].toString(), z["proprietario"].toString(),uri)
                    }
                if(checkVendita) {
                    if(!materiale?.stato.equals("venduto"))
                        list.add(materiale)
                }
                else
                    list.add(materiale)
            }
        }
        return list

    }

    private fun addStudente(name: String,surname: String,email: String,password: String) {
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

    //return -1 se non trova il corso
    private fun getCorsoId(nome: String): String{
        val i=database.collection("Corso").whereEqualTo("nome",nome).get()
        while (!i.isComplete);
        val k :MutableList<DocumentSnapshot> = i.result.documents
        return if(k.size==0)
            "-1"
        else {
            k[0].id
        }
    }

    private fun getPhotoMateriale(Uri: String): Bitmap {
        val imagereference = storageReference.child(Uri)
        val localfile: File = File.createTempFile("test", "jpg")
        val i = imagereference.getFile(localfile)
        while (!i.isComplete);
        return BitmapFactory.decodeFile(localfile.absolutePath)
    }

    private fun addAnnuncio(materialeDaAggiungere: MaterialeDaAggiungere){
        val id = addMateriale(materialeDaAggiungere.nome,materialeDaAggiungere.descrizione, materialeDaAggiungere.tipologia,materialeDaAggiungere.prezzo,materialeDaAggiungere.latitudine,materialeDaAggiungere.longitudine, "Vendita",materialeDaAggiungere.corso)
        addPhotosMateriale(materialeDaAggiungere.photos!!,id)
    }

    private fun addPhotosMateriale(photos: ArrayList<Photo>, idMateriale: String){
        photos.forEach{
                photo->
            val uri = Uri.parse(photo.localUri)
            val uriimageRemote="image/materiali/${idMateriale}/${uri.lastPathSegment}"
            val imageRef = storageReference.child(uriimageRemote)
            val uploadTask = imageRef.putFile(uri)
            while (!uploadTask.isComplete);
            uploadTask.addOnSuccessListener {
                Log.i(ContentValues.TAG, "Image uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                    photo.remoteUri = uriimageRemote
                    updatePhotoDatabaseMateriale(photo,idMateriale)
                }
            }
            uploadTask.addOnFailureListener{
                Log.e(ContentValues.TAG, it.message?: "No message")
            }
        }
    }

    private fun addPhotoStudente(photo:Photo, idstudente:String){
        deleteLastUriPhotosStudente(idstudente)
        val uri = Uri.parse(photo.localUri)
        val uriimageRemote="image/studenti/${idstudente}/${uri.lastPathSegment}"
        val imageRef = storageReference.child(uriimageRemote)
        val uploadTask = imageRef.putFile(uri)
        while (!uploadTask.isComplete);
        uploadTask.addOnSuccessListener {
            Log.i(ContentValues.TAG, "Image uploaded $imageRef")
            imageRef.downloadUrl
            photo.remoteUri = uriimageRemote
            updatePhotoDatabaseStudente(photo,idstudente)
        }
        uploadTask.addOnFailureListener{
            Log.e(ContentValues.TAG, it.message?: "No message")
        }
    }

    private fun updatePhotoDatabaseStudente(photo: Photo, idstudente: String) {
        val photoCollection = database.collection("studenti").document(idstudente).collection("photos")
        val handle = photoCollection.add(photo)
        while (!handle.isComplete);
        handle.addOnSuccessListener {
            Log.i(ContentValues.TAG, "successfully update photo metadata with"+it.id)
            photo.id=it.id
            database.collection("studenti").document(idstudente).collection("photos").document(photo.id).set(photo)
        }
        handle.addOnFailureListener{
            Log.e(ContentValues.TAG, "error updating photo data: ${it.message}")
        }
    }

    private fun getPhotoStudente(Uri: String): Bitmap{
        val imagereference = storageReference.child(Uri)
        lateinit var  bitmap: Bitmap
        val localfile : File = File.createTempFile("test","jpg")
        val i=imagereference.getFile(localfile)
        while (!i.isComplete);
        bitmap= BitmapFactory.decodeFile(localfile.absolutePath)
        return bitmap
    }

    private fun searchMateriale(corso: String): ArrayList<Materiale?> {
        val list: ArrayList<Materiale?> = ArrayList<Materiale?>()
        val h =database.collection("Corso").whereEqualTo("nome",corso).get()
        while (!h.isComplete);
        val u :MutableList<DocumentSnapshot> = h.result.documents
        if(u.isEmpty())
            return list
        val idCorso = u[0].id
        val i = database.collection("materiale").whereEqualTo("idCorso", idCorso).get()
        while (!i.isComplete);
        val k: MutableList<DocumentSnapshot> = i.result.documents
        for(z in k)
        {
            val  uri: ArrayList<String> = ArrayList()
            uri.add(getUriPhotosMateriale(z.id))
            val c= z.getGeoPoint("cordinate")
            val materiale : Materiale? =
                c?.let {
                    Materiale(z.id,z["nome"].toString(),z["descrizione"].toString(),z["tipologia"].toString(),z["prezzo"].toString().toDouble(),
                        it.latitude,it.longitude,z["stato"].toString(),z["idCorso"].toString(), z["proprietario"].toString(),uri)
                }
            if(materiale!=null && !materiale.stato.equals("venduto"))
                list.add(materiale)
        }
        return  list
    }

    //crea una funzione che restituisce lo studente dal database passando l'id dello studente
    private fun getStudenteFromId(id: String): Studente?{
        val i=database.collection("studenti").document(id).get()
        while (!i.isComplete);
        val k = i.result
        return if(k==null)
            null
        else {
            val studente =
                Studente(k.id,k["nome"].toString(),k["cognome"].toString(),k["email"].toString(),User(k["idUtente"].toString(),k["username"].toString()))
            studente
        }
    }

    private fun setUsersChat(
        userList: ArrayList<User>,
        adapter: RecyclerView.Adapter<*>,
        view: View
    ) {
        mDbref.child("users").child(getLoggedStudent().id).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(getLoggedStudent().id != currentUser?.id) {
                        userList.add(currentUser!!)
                    }
                }
                if(userList.isEmpty()) {
                    view.findViewById<RecyclerView>(R.id.rv_chats).visibility = View.GONE
                    view.findViewById<TextView>(R.id.tv_no_chats).visibility = View.VISIBLE
                } else {
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun updatePhotoDatabaseMateriale(photo:Photo, idMateriale: String) {
        val photoCollection = database.collection("materiale").document(idMateriale).collection("photos")
        val handle = photoCollection.add(photo)
        handle.addOnSuccessListener {
            Log.i(ContentValues.TAG, "successfully update photo metadata with"+it.id)
            photo.id=it.id
            database.collection("materiale").document(idMateriale).collection("photos").document(photo.id).set(photo)
        }
        handle.addOnFailureListener{
            Log.e(ContentValues.TAG, "error updating photo data: ${it.message}")
        }

    }

    //return -1 se non trova il corso
    private fun addMateriale(nome : String, descrizione : String, tipologia: String,prezzo : Double,latitudine : Double?,longitudine : Double?,stato : String ,NomeCorso : String ): String{
        val c = GeoPoint(latitudine!!,longitudine!!)
        val corso=getCorsoId(NomeCorso)
        if(corso == "-1")
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
        val s= database.collection("materiale").add(materiale).addOnSuccessListener {
                documentReference ->
            id=documentReference.id
            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${id}")
        }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

        while(!s.isComplete);
        id=s.result.id
        return id
    }

    private fun getStudente(username : String) : Studente? {
        val i = database.collection("studenti").whereEqualTo("email",username).get()
        while (!i.isComplete);
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

    fun getLastMessage(studenteRichiesto : User) : Messaggio? {
        val senderRoom= Database.getLoggedStudent().id+studenteRichiesto.id
        val messageList= ArrayList<Messaggio>()
        val snapshot= mDbref.child("chats").child(senderRoom).child("messages").get()
        while (!snapshot.isComplete);
            val x = snapshot.result.children
            Log.i("firebase", "Got value ${snapshot}")
            for (doc in x) {
                val message = doc.getValue(Messaggio::class.java)
                messageList.add(message!!)
        }
        return if(messageList.isEmpty())
            null
        else
            messageList[messageList.size-1]
    }

    fun registerTransaction(idAcquirente:String, materiale: Materiale) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        val transazione = hashMapOf(
            "DataTrasazione" to formatted,
            "idAcquirente" to idAcquirente,
            "idMateriale" to materiale.id,
            "idVenditore" to loggedStudente.utente?.id,
            "prezzo" to materiale.prezzo
        )
        database.collection("Transazioni")
            .add(transazione)
        database.collection("materiale").document(materiale.id).update("stato","venduto")
        database.collection("materiale").document(materiale.id).update("proprietario",idAcquirente)

    }

    fun getTransaction(): List<Materiale?>{
        val listReturn = ArrayList<Materiale?>()
        val h = database.collection("Transazioni").whereEqualTo("idVenditore",Database.getLoggedStudent().id).get()
        while (!h.isComplete);
        val u :MutableList<DocumentSnapshot> = h.result.documents
        for(z in u) {
            val transazione = z["idMateriale"] as String
            val materiale= getMaterialeFromid(transazione)
            if(materiale != null)
                listReturn.add(materiale)
        }

        val x = database.collection("Transazioni").whereEqualTo("idAcquirente",Database.getLoggedStudent().id).get()
        while (!x.isComplete);
        val l :MutableList<DocumentSnapshot> = x.result.documents
        for(c in l) {
            val transazione = c["idMateriale"] as String
            val materiale= getMaterialeFromid(transazione)
            if(materiale != null) {
                materiale.stato="acquistato"
                listReturn.add(materiale)
            }
        }

        return listReturn
    }

    fun getMaterialeFromid(id:String): Materiale? {
        var materiale: Materiale? = null
        val i= database.collection("materiale").document(id).get()
        while (!i.isComplete);
        val z : DocumentSnapshot = i.result
        if(z.exists()) {
            val uri: ArrayList<String> = ArrayList()
            uri.add(getUriPhotosMateriale(z.id))
            val c= z.getGeoPoint("cordinate")
            materiale =
                c?.let {
                    Materiale(z.id,z["nome"].toString(),z["descrizione"].toString(),z["tipologia"].toString(),z["prezzo"].toString().toDouble(),
                        it.latitude,it.longitude,z["stato"].toString(),z["idCorso"].toString(), z["proprietario"].toString(),uri)
                }
        }
        return materiale
    }

    companion object {
        private lateinit var istance: Database

        private fun getIstance(): Database {
            if (!this::istance.isInitialized)
                istance = Database()
            return istance
        }

        fun inizializateDatabase(): Boolean {
            getIstance()
            return this::istance.isInitialized
        }

        fun setLoggedStudent(studente: Studente) {
            getIstance().setLoggedStudent(studente)
        }

        fun getLoggedStudent(): Studente {
            return getIstance().loggedStudente
        }

        fun getStudente(email: String): Studente? {
            return getIstance().getStudente(email)
        }

        fun addStudente(name: String, surname: String, email: String, password: String) {
            getIstance().addStudente(name, surname, email, password)
        }

        fun getLastMessage(studenteRichiesto: User): Messaggio? {
            return getIstance().getLastMessage(studenteRichiesto)
        }

        fun getMateriali(): ArrayList<Materiale> {
            return getIstance().getMateriali()
        }

        fun addAnnuncio(materiale: MaterialeDaAggiungere) {
            getIstance().addAnnuncio(materiale)
        }

        fun getPhotoMateriale(uri: String): Bitmap {
            return getIstance().getPhotoMateriale(uri)
        }

        fun getMaterialiStudente(username: String,checkVendita: Boolean): ArrayList<Materiale?> {
            return getIstance().getMaterialiStudente(username, checkVendita)
        }

        fun searchMateriale(corso: String): ArrayList<Materiale?> {
            return getIstance().searchMateriale(corso)
        }

        fun getStudenteFromId(id: String): Studente? {
            return getIstance().getStudenteFromId(id)
        }

        fun registerTransaction(idAcquirente: String, materiale: Materiale) {
            getIstance().registerTransaction(idAcquirente, materiale)
        }

        fun addPhotoStudente(photo: Photo, idstudente: String) {
            getIstance().addPhotoStudente(photo, idstudente)
        }

        fun getPhotoStudente(Uri: String): Bitmap {
            return getIstance().getPhotoStudente(Uri)
        }

        fun setUsersChat(userList : ArrayList<User>, adapter : RecyclerView.Adapter<*>, view : View) {
            return getIstance().setUsersChat(userList, adapter, view)
        }
        fun getTransaction(): List<Materiale?> {
            return getIstance().getTransaction()
        }
        fun getUriPhotosStudente(idStudente: String) : String{
            return getIstance().getUriPhotosStudente(idStudente)
        }

    }
}