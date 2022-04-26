package com.example.usedbooks.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.usedbooks.R
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MaterialeFragment : Fragment() {

    val args : MaterialeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_materiale, container, false)

        val materiale = args.materiale

        val tv_nome_materiale = view.findViewById<TextView>(R.id.tv_nome_materiale)
        tv_nome_materiale.setText(materiale.nome)
        val tv_nome_venditore = view.findViewById<TextView>(R.id.tv_nome_venditore)
        tv_nome_venditore.setText(materiale.proprietario)
        val tv_prezzo = view.findViewById<TextView>(R.id.tv_prezzo)
        tv_prezzo.setText(materiale.prezzo.toString())

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.mv_materiale) as SupportMapFragment?
        supportMapFragment?.getMapAsync {
            it.addMarker(MarkerOptions().title("Posizione Materiale").position(LatLng(materiale.latitudine, materiale.Longitudine)))
        }
        return view
    }
}