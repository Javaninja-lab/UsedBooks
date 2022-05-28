package com.example.usedbooks.main.nuovoAnnuncio

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Materiale
import com.example.usedbooks.dataClass.MaterialeDaAggiungere
import com.example.usedbooks.main.home.HomeFragmentDirections

class DatiFragment : Fragment() {

    lateinit var materialeDaInviare : MaterialeDaAggiungere

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nuovo_annuncio_dati, container, false)

        val et_title_new_annuncio = view.findViewById<EditText>(R.id.et_title_new_annuncio)
        val et_descrizione_new_annuncio = view.findViewById<EditText>(R.id.et_descrizione_new_annuncio)
        val et_tipologia_new_annuncio = view.findViewById<EditText>(R.id.et_tipologia_new_annuncio)
        val et_prezzo_new_annuncio = view.findViewById<EditText>(R.id.et_prezzo_new_annuncio)
        val bt_invio_dati = view.findViewById<Button>(R.id.bt_invio_dati)

        bt_invio_dati.setOnClickListener {
            materialeDaInviare = MaterialeDaAggiungere(et_title_new_annuncio.text.toString(),
                et_descrizione_new_annuncio.text.toString(), et_tipologia_new_annuncio.text.toString(),
                et_prezzo_new_annuncio.text.toString().toDouble())
            val action = DatiFragmentDirections.actionDatiFragmentToMapsFragment(materialeDaInviare)
            findNavController().navigate(action)
        }

        return view
    }

}