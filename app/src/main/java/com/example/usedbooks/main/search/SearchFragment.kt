package com.example.usedbooks.main.search

import android.os.Bundle
import android.provider.ContactsContract
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
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val recyclerView  = view.findViewById<RecyclerView>(R.id.rv_search)
        val adapter = MaterialeRecyclerAdapter("search", false)
        recyclerView?.adapter = adapter
        val response: ArrayList<Materiale> = ArrayList()
        adapter.submitList(response)

        val bt_search = view.findViewById<Button>(R.id.bt_search)
        val cl_search = view.findViewById<ConstraintLayout>(R.id.cl_search)
        val tv_nessun_materiale = view.findViewById<View>(R.id.tv_nessun_materiale)
        val pb_caricamento = PersonalProgressBar(view.context, cl_search)
        pb_caricamento.caricamento {
            response.clear()
            for (materiale in Database.getMateriali()) {
                response.add(materiale)
            }
            pb_caricamento.post {
                pb_caricamento.visibility = View.GONE
                if (response.isEmpty()) {
                    tv_nessun_materiale.visibility = View.VISIBLE
                    bt_search.isEnabled = false
                } else {
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }

        bt_search?.setOnClickListener {
            recyclerView.visibility = View.GONE
            tv_nessun_materiale.visibility = View.GONE
            pb_caricamento.visibility = View.VISIBLE
            val search = view.findViewById<EditText>(R.id.et_cerca_annunci)?.text.toString()
            response.clear()

            pb_caricamento.caricamento {
                if(search == "") {
                    for(materiale in Database.getMateriali()){
                        response.add(materiale)
                    }
                } else {
                    for (materiale in Database.searchMateriale(search)) {
                        if (materiale != null)
                            response.add(materiale)
                    }
                }
                recyclerView.post {
                    pb_caricamento.visibility = View.GONE
                    if (response.isEmpty()) {
                        tv_nessun_materiale.visibility = View.VISIBLE
                        Toast.makeText(context, "Nessun risultato", Toast.LENGTH_LONG).show()
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        pb_caricamento.visibility = View.GONE
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
        return view
    }
}