package com.example.usedbooks.main.nuovoAnnuncio

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.MaterialeDaAggiungere
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {

    val args : MapsFragmentArgs by navArgs()
    lateinit var materialeDaAggiungere: MaterialeDaAggiungere
    var latitudine : Double = -34.0
    var longitudine : Double = 151.0

    private val callback = OnMapReadyCallback { googleMap ->
        impostaCordinata(googleMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nuovo_annuncio_maps, container, false)

        //materialeDaAggiungere = args.materialeProvvisorio

        val bt_invio_dati = view.findViewById<Button>(R.id.bt_invio_dati)
        bt_invio_dati.setOnClickListener {
            //materialeDaAggiungere = MaterialeDaAggiungere(materialeDaAggiungere, latitudine, longitudine)
            findNavController().navigate(R.id.action_mapsFragment_to_cameraFragment)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun impostaCordinata(googleMap: GoogleMap) {
        val position = LatLng(latitudine, longitudine)
        googleMap.addMarker(MarkerOptions().position(position).title("Position of Material"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
    }

    fun getPostion() {
        val locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGPSEnabled) {

        }
    }

    private val requestMultiplePermissionLaucher = registerForActivityResult (ActivityResultContracts.RequestMultiplePermissions()) { resultsMap ->
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
            getPostion()
        }
        else {
            Toast.makeText(this.requireContext(),"Unable to get position without permission.", Toast.LENGTH_LONG).show()
        }
    }
}