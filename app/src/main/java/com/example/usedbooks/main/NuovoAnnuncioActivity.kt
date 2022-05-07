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
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class NuovoAnnuncioActivity : AppCompatActivity() {
    private var strUri :String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuovo_annuncio)
        val map = findViewById<MapView>(R.id.mv_profile)
        map.getMapAsync {
            it.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Prova Marker"))
        }

        val addImage = findViewById<Button>(R.id.addImage)
        addImage.setOnClickListener {
            takePhoto(this)
            Log.i(TAG, strUri)
            val imgFile = File(strUri)
            val image = findViewById<ImageView>(R.id.immagineTest)
            image.setImageURI(uri)
            if(imgFile.exists()) {

               // val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

                //image.setImageBitmap(myBitmap)
            }


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
        getCameraImage.launch(uri)
        Log.i(TAG, "l'uri è"+uri)
        strUri= "com.example.usedbooks.fileprovider"+uri?.path.toString()

    }

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

}
