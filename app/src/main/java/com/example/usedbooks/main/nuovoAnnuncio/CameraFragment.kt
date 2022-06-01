package com.example.usedbooks.main.nuovoAnnuncio

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.MaterialeDaAggiungere
import com.example.usedbooks.dataClass.Photo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.ArrayList

class CameraFragment : Fragment() {

    val args : CameraFragmentArgs by navArgs()
    lateinit var materialeDaAggiungere: MaterialeDaAggiungere

    //TODO chiamare saveimage quando si inviano tutti i dati
    var strUri :String=""
    var photos: ArrayList<Photo> = ArrayList<Photo>()
    private lateinit var immagineTest : ImageView

    private  var firestore= Firebase.firestore
    private var storageReference = FirebaseStorage.getInstance().getReference()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nuovo_annuncio_camera, container, false)

        val gallery = view.findViewById<Button>(R.id.gallery)
        gallery.setOnClickListener {
            startGallery.launch("image/*")
        }

        immagineTest = view.findViewById(R.id.iv_foto_scelta)

        materialeDaAggiungere = args.materialeProvvisorio

        val btn_send_data = view.findViewById<Button>(R.id.btn_send_data)
        btn_send_data.setOnClickListener {
            Database.addAnnuncio(materialeDaAggiungere)
        }

        val btn= view.findViewById<Button>(R.id.buttonTest)
        btn.setOnClickListener {
            /*val imagereference = storageReference.child("image/"+Database.getLoggedStudent().id+"/image:62")
            val localfile :File= File.createTempFile("test","jpg")
            imagereference.getFile(localfile).addOnSuccessListener {
                val bitmap: Bitmap= BitmapFactory.decodeFile(localfile.absolutePath)
                immagineTest.setImageBitmap(bitmap)
            }*/
            Database.getUriPhotoMateriale(
                    "Jt35xdpSyzEgrUUgAotV")
        }

        val addImage = view.findViewById<Button>(R.id.addImage)
        addImage.setOnClickListener {
            takePhoto(this.requireContext())
        }

        return view
    }

    fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startCamera.launch(cameraIntent)
    }

    val startCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data

            if(data!=null) {
                immagineTest.setImageBitmap(data.extras?.get("data") as Bitmap)
                val uri= getImageUri(this.requireContext(),data.extras?.get("data") as Bitmap)
                Log.i("uri","${uri}")
                val photo = Photo(localUri = uri.toString())
                photos.add(photo)
                saveImage(photos)
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    val startGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val image = this.requireView().findViewById<ImageView>(R.id.iv_foto_scelta)
        image.setImageURI(it)
        val photo = Photo(localUri = it.toString())
        Log.i("uri","${it.toString()}")
        photos.add(photo)
        saveImage(photos)
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
            capturePhoto()
        }
        else {
            Toast.makeText(this.requireContext(),"Unable to load camera without permission.", Toast.LENGTH_LONG).show()
        }
    }

    fun takePhoto(context : Context){
        if(hasCameraPermission(context) == PackageManager.PERMISSION_GRANTED && hasExternalStoregaePermission(context)== PackageManager.PERMISSION_GRANTED) {
            capturePhoto()
        }
        else {
            requestMultiplePermissionLaucher.launch(arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ))
        }
    }


    /*private fun createImageFile(): File {
        val timestamp= SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Specimen_${timestamp}",
            ".jpg",
            imageDirectory
        ).apply {
            currentImgepath= absolutePath
        }
    }*/

    fun hasCameraPermission(context : Context)= ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
    fun hasExternalStoregaePermission(context : Context)= ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


    private fun saveImage(photos: ArrayList<Photo>){
        if(photos.isNotEmpty()) {
            materialeDaAggiungere.photos=photos
        }
    }

    /*private fun uploadPhotos() {
        photos.forEach{
                photo->
            var uri = Uri.parse(photo.localUri)
            val imageRef = storageReference.child("image/${Database.getLoggedStudent().id}/${uri.lastPathSegment}")
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                Log.i(ContentValues.TAG, "Image uploaded $imageRef")
                val downloadUrl = imageRef.downloadUrl
                downloadUrl.addOnSuccessListener {
                        remoteUri->
                    photo.remoteUri = remoteUri.toString()
                    updatePhotoDatabase(photo)
                }
            }
            uploadTask.addOnFailureListener{
                Log.e(ContentValues.TAG, it.message?: "No message")
            }
        }
    }*/

    /*private fun updatePhotoDatabase(photo: Photo) {
        var photoCollection = firestore.collection("studenti").document(Database.getLoggedStudent().id).collection("photos")
        var handle = photoCollection.add(photo)
        handle.addOnSuccessListener {
            Log.i(ContentValues.TAG, "successfully update photo metadata")
            photo.id=it.id
            var photoCollection = firestore.collection("studenti").document(Database.getLoggedStudent().id).collection("photos").document(photo.id).set(photo)
        }
        handle.addOnFailureListener{
            Log.e(ContentValues.TAG, "error updating photo data: ${it.message}")
        }
    }*/


}