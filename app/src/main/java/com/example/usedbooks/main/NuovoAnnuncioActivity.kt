package com.example.usedbooks.main

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Gestore
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.jar.Manifest

class NuovoAnnuncioActivity : AppCompatActivity() {
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
        }
    }
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




    fun invokeCamera() {

    }

    fun hasCameraPermission(context : Context)= ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
    fun hasExternalStoregaePermission(context : Context)= ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

}
