package com.example.usedbooks.main

import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Photo
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NuovoAnnuncioActivity : AppCompatActivity() {
    var strUri :String=""
    var photos: ArrayList<Photo> = ArrayList<Photo>()

    private  var firestore= Firebase.firestore
    private var storageReference = FirebaseStorage.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuovo_annuncio)

        val addImage = findViewById<Button>(R.id.addImage)
        addImage.setOnClickListener {
            takePhoto(this)
            Log.i(TAG, strUri)
            val imgFile = File(strUri)
            val image = findViewById<ImageView>(R.id.immagineTest)
            image.setImageURI(uri)
            val photo = photos[0]
            saveImage(photos)

            //if(imgFile.exists()) {

               // val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

                //image.setImageBitmap(myBitmap)
            //}


        }

    }


    private var uri: Uri?= null

    private lateinit var currentImgepath: String



    private val requestMultiplePermissionLaucher= registerForActivityResult (ActivityResultContracts.RequestMultiplePermissions()) { resultsMap ->
        var permissionGranted = false
        resultsMap.forEach {
            if (it.value == true) {
                permissionGranted = it.value
            } else {
                permissionGranted= false
                return@forEach
            }
        }
        if (permissionGranted){
            invokeCamera()
        }
        else
        {
            Toast.makeText(this,"unable to load camera without permission.", Toast.LENGTH_LONG).show()
        }
    }

    fun takePhoto(context : Context){
        if(hasCameraPermission(context)== PackageManager.PERMISSION_GRANTED && hasExternalStoregaePermission(context)== PackageManager.PERMISSION_GRANTED)
        {
            invokeCamera()
        }
        else {
            requestMultiplePermissionLaucher.launch(arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA

            ))
        }
    }


    private fun createImageFile(): File {
        val timestamp= SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Specimen_${timestamp}",
            ".jpg",
            imageDirectory
        ).apply {
            currentImgepath= absolutePath
        }
    }

    fun invokeCamera() {
        val file = createImageFile()
        try{
            uri = FileProvider.getUriForFile(this,"com.example.usedbooks.fileprovider",file)
        }
        catch (e: Exception){
            Log.e(TAG, " ${e.message} ")
            var foo= e.message
        }
        val i=0
        getCameraImage.launch(uri)
        Log.i(TAG, "l'uri è"+uri)
        strUri= "com.example.usedbooks.fileprovider"+uri?.path.toString()

        val photo = Photo(localUri = uri.toString())
        photos.add(photo)



    }

    var i =0

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()){

        success-> if (success){
            Log.i(TAG,"Image Location: $uri")


        }
        else
    {
        Log.i(TAG,"Image Location: $uri")
    }
    }

    fun hasCameraPermission(context : Context)= ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
    fun hasExternalStoregaePermission(context : Context)= ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


    private fun saveImage(photos: ArrayList<Photo>){
        if(photos.isNotEmpty())
        {
            uploadPhotos()
        }
    }

    private fun uploadPhotos() {
        photos.forEach{
            photo->
            var uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("image/${Database.getLoggedStudent().id}/${uri.lastPathSegment}")
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(TAG, "Image uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener { 
                    remoteUri->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo)
                }
            }
            uploadTask.addOnFailureListener{
                Log.e(TAG, it.message?: "No message")
            }
        }
    }

    private fun updatePhotoDatabase(photo: Photo) {

        var photoCollection = firestore.collection("studenti").document(Database.getLoggedStudent().id).collection("photos")
        var handle = photoCollection.add(photo)
        handle.addOnSuccessListener {
            Log.i(TAG, "successfully update photo metadata")
            photo.id=it.id
            var photoCollection = firestore.collection("studenti").document(Database.getLoggedStudent().id).collection("photos").document(photo.id).set(photo)
        }
        handle.addOnFailureListener{
            Log.e(TAG, "error updating photo data: ${it.message}")
        }
    }


}
