package com.example.usedbooks.dataClass

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest
import java.util.jar.Manifest
import kotlin.concurrent.thread

abstract class Gestore {
    companion object {
        val md = MessageDigest.getInstance("SHA-256")

        fun ByteArray.toHex() = joinToString(separator = "") {
            "%02x".format(it)
        }

        fun getHash(input : String) : String {
            return md.digest(input.toByteArray(UTF_8)).toHex()
        }

        fun setBitmap(materiale : Materiale, imageView: ImageView) {
            thread(start = true) {
                val photoBitmap : Bitmap = Database.getPhotoMateriale(materiale.photos[0])
                imageView.post {
                    imageView.setImageBitmap(photoBitmap)
                }
            }
        }

    }
}