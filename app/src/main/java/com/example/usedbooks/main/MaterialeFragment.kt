package com.example.usedbooks.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Gestore
import com.example.usedbooks.dataClass.User
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


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
        val iv_foto_materiale = view.findViewById<ImageView>(R.id.iv_foto_materiale)
        Gestore.setBitmap(materiale, iv_foto_materiale)

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.mv_materiale) as SupportMapFragment?
        supportMapFragment?.getMapAsync {
            val position = LatLng(materiale.latitudine, materiale.longitudine)
            it.addMarker(MarkerOptions().title("Posizione Materiale").position(position))
            it.moveCamera(CameraUpdateFactory.newLatLng(position))
            it.moveCamera(CameraUpdateFactory.zoomTo(13F))
        }

        val btnContact = view.findViewById<Button>(R.id.btn_contact)
        btnContact.setOnClickListener {
                var mDbRef: DatabaseReference= FirebaseDatabase.getInstance().getReference()
                var NomeProprietario = Database.getStudenteFromId(materiale.proprietario)?.nome
                mDbRef.child("users").child(Database.getLoggedStudent().id).child(materiale.proprietario).setValue(
                    User(materiale.proprietario,NomeProprietario)
                )
            //TODO andare al fragment ListChatFragment

        }
        return view
    }
}