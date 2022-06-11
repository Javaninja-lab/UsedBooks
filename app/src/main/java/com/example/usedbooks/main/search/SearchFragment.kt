package com.example.usedbooks.main.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
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

        val recyclerView  = view?.findViewById<RecyclerView>(R.id.ListViewSearch)
        val adapter = MaterialeRecyclerAdapter()
        recyclerView?.adapter = adapter
        var response: ArrayList<Materiale> = ArrayList()
        adapter.submitList(response)

        val btn_search = view?.findViewById<Button>(R.id.searchBtn)
        btn_search?.setOnClickListener {

            val search = view?.findViewById<EditText>(R.id.et_cerca_annunci)?.text.toString()

            thread(start = true) {
                response.clear()
                for ( materiale in Database.searchMateriale(search)){
                    if(materiale==null)
                        Toast.makeText(requireContext(),"nessun risultato", Toast.LENGTH_LONG).show()
                    else
                        response.add(materiale)
                }
                this.activity?.runOnUiThread{
                    adapter.notifyDataSetChanged()
                }

            }
        }
        return layout
    }
}