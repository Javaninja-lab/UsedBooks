package com.example.usedbooks.main.nuovoAnnuncio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.usedbooks.R

class DatiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nuovo_annuncio_dati, container, false)
        val bt_invio_dati = view.findViewById<Button>(R.id.bt_invio_dati)
        bt_invio_dati.setOnClickListener {
            findNavController().navigate(R.id.action_datiFragment_to_mapsFragment)
        }
        return view
    }

}