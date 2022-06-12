package com.example.usedbooks.main.home

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.adapters.MaterialeRecyclerAdapter
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import kotlin.concurrent.thread


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

        val pb_caricamento = view.findViewById<ProgressBar>(R.id.pb_caricamento)
        val progressDrawable: Drawable = pb_caricamento.getIndeterminateDrawable().mutate()
        val typedValue = TypedValue()
        context?.getTheme()?.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        val color = typedValue.data
        progressDrawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        pb_caricamento.progressDrawable = progressDrawable

        val response: ArrayList<Materiale> = ArrayList()
        adapter.submitList(response)

        thread(start = true) {
            response.clear()
            for (materiale in Database.getMateriali()){
                response.add(materiale)
            }
            this.activity?.runOnUiThread{
                recyclerView.visibility = View.VISIBLE
                pb_caricamento.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }

        }





        return view
    }
}