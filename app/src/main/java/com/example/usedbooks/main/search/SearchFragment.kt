package com.example.usedbooks.main.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.customView.PersonalProgressBar
import com.example.usedbooks.R
import com.example.usedbooks.adapters.MaterialeRecyclerAdapter
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import kotlin.concurrent.thread


class SearchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_search, container, false)

        val recyclerView  = layout.findViewById<RecyclerView>(R.id.rv_search)
        val adapter = MaterialeRecyclerAdapter("search", false)
        recyclerView?.adapter = adapter
        val response: ArrayList<Materiale> = ArrayList()
        adapter.submitList(response)

        val cl_search = layout.findViewById<ConstraintLayout>(R.id.cl_search)
        val pb_caricamento = PersonalProgressBar(layout.context, cl_search)
        pb_caricamento.caricamento(response, recyclerView)

        val bt_search = layout.findViewById<Button>(R.id.bt_search)
        bt_search?.setOnClickListener {
            recyclerView.visibility = View.GONE
            pb_caricamento.visibility = View.VISIBLE
            val search = layout.findViewById<EditText>(R.id.et_cerca_annunci)?.text.toString()
            response.clear()

            thread(start = true) {
                for (materiale in Database.searchMateriale(search)){
                    if(materiale!=null)
                        response.add(materiale)
                }
                recyclerView.post {
                    recyclerView.visibility = View.VISIBLE
                    pb_caricamento.visibility = View.GONE
                    if(response.size==0)
                        Toast.makeText(context, "Nessun risultato", Toast.LENGTH_LONG).show()
                    else
                        adapter.notifyDataSetChanged()
                }
            }
        }
        return layout
    }
}