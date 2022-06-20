package com.example.usedbooks.main.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Photo
import java.io.ByteArrayOutputStream
import java.util.ArrayList

class ChangeFotoProfiloFragment : Fragment() {
    private val photos: ArrayList<Photo> = ArrayList<Photo>()
    private lateinit var iv_foto_profilo_nuovo: ImageView
    private lateinit var iv_foto_profilo_attuale: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_foto_profilo, container, false)
        iv_foto_profilo_attuale = view.findViewById(R.id.iv_foto_profilo_attuale)
        iv_foto_profilo_nuovo = view.findViewById(R.id.iv_foto_profilo_nuovo)

        val btn_image_gallery = view.findViewById<Button>(R.id.btn_image_gallery)
        btn_image_gallery.setOnClickListener {
            startGallery.launch("image/*")
        }
        val btn_image_camera = view.findViewById<Button>(R.id.btn_image_camera)
        btn_image_camera.setOnClickListener {
            takePhoto(view.context)
        }

        val btn_image_confirm = view.findViewById<Button>(R.id.btn_image_confirm)
        btn_image_confirm.setOnClickListener {
            if(photos.isEmpty()) {
                Toast.makeText(view.context, "Seleziona un'immagine", Toast.LENGTH_SHORT).show()
            } else {
                saveImage(photos)
                activity?.onBackPressed()
            }
        }

        val uriImageStudent= Database.getUriPhotosStudente(Database.getLoggedStudent().id)
        if(uriImageStudent != "")
            iv_foto_profilo_attuale.setImageBitmap(Database.getPhotoStudente(uriImageStudent))
        else {
            iv_foto_profilo_attuale.setImageResource(R.drawable.placeholder)
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
                iv_foto_profilo_nuovo.setImageBitmap(data.extras?.get("data") as Bitmap)
                val uri= getImageUri(this.requireContext(),data.extras?.get("data") as Bitmap)
                Log.i("uri","$uri")
                val photo = Photo(localUri = uri.toString())
                photos.add(photo)
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
        //val image = this.requireView().findViewById<ImageView>(R.id.iv_foto_scelta)
        iv_foto_profilo_nuovo.setImageURI(it)
        val photo = Photo(localUri = it.toString())
        Log.i("uri", it.toString())
        photos.add(photo)

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

    fun hasCameraPermission(context : Context)= ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
    fun hasExternalStoregaePermission(context : Context)= ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


    private fun saveImage(photos: ArrayList<Photo>){
        Database.addPhotoStudente(photos[0],Database.getLoggedStudent().id)
        Toast.makeText(iv_foto_profilo_attuale.context, "Image saved, please wait some time to update the image", Toast.LENGTH_SHORT).show()
    }

}