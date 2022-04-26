package com.example.usedbooks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class NuovoAnnuncioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuovo_annuncio)
        val map = findViewById<MapView>(R.id.mv_profile)
        map.getMapAsync {
            it.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Prova Marker"))
        }
    }
}