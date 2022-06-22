package com.example.usedbooks.main.nuovoAnnuncio

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {
    val args : MapsFragmentArgs by navArgs()
    lateinit var materialeDaAggiungere: MaterialeDaAggiungere

    var locationByGPS : Location? = null
    var locationByNetwork : Location? = null
    lateinit var map : GoogleMap
    var latitudine : Double = -34.0
    var longitudine : Double = 151.0
    var marker : Marker? = null

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.moveCamera(CameraUpdateFactory.zoomTo(13F))
        impostaCordinata()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nuovo_annuncio_maps, container, false)

        materialeDaAggiungere = args.materialeProvvisorio
        if(materialeDaAggiungere.latitudine != null && materialeDaAggiungere.longitudine != null) {
            latitudine = materialeDaAggiungere.latitudine!!
            longitudine = materialeDaAggiungere.longitudine!!
        }

        val bt_current_position = view.findViewById<Button>(R.id.bt_current_position)
        bt_current_position.setOnClickListener {
            getPostion()
        }

        val bt_invio_dati = view.findViewById<Button>(R.id.bt_invio_dati)
        bt_invio_dati.setOnClickListener {
            materialeDaAggiungere = MaterialeDaAggiungere(materialeDaAggiungere, latitudine, longitudine)
            val action =
                MapsFragmentDirections.actionMapsFragmentToCameraFragment(materialeDaAggiungere)
            findNavController().navigate(action)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun impostaCordinata() {
        val position = LatLng(latitudine, longitudine)
        marker?.remove()
        marker = map.addMarker(MarkerOptions().position(position).title("Position of Material"))
        map.moveCamera(CameraUpdateFactory.newLatLng(position))
    }

    @SuppressLint("MissingPermission")
    fun getPostion() {
        if(isLocationPermissionGranted()) {
            val locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    gpsLocationListener
                )
                val lastKnownLocationByGps =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                lastKnownLocationByGps?.let {
                    locationByGPS = lastKnownLocationByGps
                }
            } else {
                Toast.makeText(requireContext(), "GPS not activated", Toast.LENGTH_LONG).show()
            }
            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0F,
                    networkLocationListener
                )
                val lastKnownLocationByNetwork =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                lastKnownLocationByNetwork?.let {
                    locationByNetwork = lastKnownLocationByNetwork
                }
            }
            var currentLocation : Location? = null
            if (locationByGPS != null && locationByNetwork != null) {
                currentLocation = if (locationByGPS!!.accuracy > locationByNetwork!!.accuracy) {
                    locationByGPS
                } else {
                    locationByNetwork
                }
            } else {
                if(locationByGPS != null || locationByNetwork != null){
                    if(locationByGPS!=null){
                        currentLocation = locationByGPS
                    } else {
                        currentLocation = locationByNetwork
                    }
                }
            }
            if(currentLocation!=null){
                latitudine = currentLocation.latitude
                longitudine = currentLocation.longitude
                impostaCordinata()
            } else {
                Toast.makeText(requireContext(), "Unable to ger you current position, please retry again later or contact support", Toast.LENGTH_LONG).show()
            }
        } else {
            requestMultiplePermissionLaucher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)
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
            getPostion()
        }
        else {
            Toast.makeText(this.requireContext(),"Unable to load position without permission.", Toast.LENGTH_LONG).show()
        }
    }

    val gpsLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationByGPS = location
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private val networkLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationByNetwork = location
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
}