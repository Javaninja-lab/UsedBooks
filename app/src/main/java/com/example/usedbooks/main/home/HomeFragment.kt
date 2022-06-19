package com.example.usedbooks.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.customView.PersonalProgressBar
import com.example.usedbooks.R
import com.example.usedbooks.adapters.MaterialeRecyclerAdapter
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView  = view.findViewById<RecyclerView>(R.id.lv_home)
        val adapter = MaterialeRecyclerAdapter("home", false)
        recyclerView.adapter = adapter

        val response: ArrayList<Materiale> = ArrayList()
        adapter.submitList(response)

        val pb_caricamento =
            PersonalProgressBar(view.context, view.findViewById(R.id.cl_lv_home))
        pb_caricamento.caricamento {
            response.clear()
            for (materiale in Database.getMateriali()) {
                response.add(materiale)
            }
            pb_caricamento.post {
                pb_caricamento.visibility = View.GONE
                if (response.isEmpty()) {
                    val tv_nessun_materiale = view.findViewById<View>(R.id.tv_nessun_materiale)
                    tv_nessun_materiale.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }

        return view
    }
}