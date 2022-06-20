package com.example.usedbooks.main.nuovoAnnuncio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Corso
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.MaterialeDaAggiungere


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
        val sp_corsi = view.findViewById<Spinner>(R.id.sp_corsi)
        val bt_invio_dati = view.findViewById<Button>(R.id.bt_invio_dati)

        val adapter : ArrayAdapter<String> = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, Database.getAllCorsi())
        //val adapter = SpinnerAdapter(this.context!!, R.layout.spinner_corsi_riga, Database.getAllCorsi())
        sp_corsi.adapter = adapter

        bt_invio_dati.setOnClickListener {
            val title : String = et_title_new_annuncio.text.toString()
            val descrizione : String = et_descrizione_new_annuncio.text.toString()
            val tipologia : String = et_tipologia_new_annuncio.text.toString()
            val corso : String = sp_corsi.selectedItem.toString()
            val prezzo : Double = if (et_prezzo_new_annuncio.text.toString().isEmpty()) (-1.0) else (et_prezzo_new_annuncio.text.toString().toDouble());

            if(!title.isEmpty() && !descrizione.isEmpty() && !tipologia.isEmpty() && !corso.isEmpty() && prezzo > 0) {
                materialeDaInviare = MaterialeDaAggiungere(title, descrizione, tipologia, corso, prezzo)
                val action =
                    DatiFragmentDirections.actionDatiFragmentToMapsFragment(materialeDaInviare)
                findNavController().navigate(action)
            } else {
                Toast.makeText(view.context, "Error in submitted data", Toast.LENGTH_SHORT).show()
            }

            //findNavController().navigate(R.id.action_datiFragment_to_mapsFragment)
        }

        return view
    }

}