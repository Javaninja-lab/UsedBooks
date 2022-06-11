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

        val recyclerView  = layout.findViewById<RecyclerView>(R.id.rv_search)
        val adapter = MaterialeRecyclerAdapter("search")
        recyclerView?.adapter = adapter
        val response: ArrayList<Materiale> = ArrayList()
        adapter.submitList(response)

        thread(start = true) {
            response.clear()
            for (materiale in Database.getMateriali()){
                response.add(materiale)
            }
            this.activity?.runOnUiThread{
                adapter.notifyDataSetChanged()
            }

        }

        val bt_search = layout.findViewById<Button>(R.id.bt_search)
        bt_search?.setOnClickListener {

            val search = it.findViewById<EditText>(R.id.et_cerca_annunci)?.text.toString()
            response.clear()
            adapter.notifyDataSetChanged()

            thread(start = true) {
                for (materiale in Database.searchMateriale(search)){
                    if(materiale!=null)
                        response.add(materiale)
                }
                this.activity?.runOnUiThread{
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