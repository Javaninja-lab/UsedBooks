package com.example.usedbooks.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import com.example.usedbooks.dataClass.Studente
import com.example.usedbooks.dataClass.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SoldFragment : Fragment() {

    val args : SoldFragmentArgs by navArgs()
    lateinit var materiale: Materiale

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_sold, container, false)

        materiale = args.materiale

        val tv_title_sold = layout.findViewById<TextView>(R.id.tv_title_sold)
        tv_title_sold.text = "Selezionare il cliente che ha comprato ${materiale.nome}. " +
                "Nel caso si voglia eliminare l'annuncio si selezioni il nome Altro"

        return layout
    }
}