package com.example.usedbooks.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.SharedElementCallback
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView  = view.findViewById<RecyclerView>(R.id.lv_home)
        val adapter = MaterialeRecyclerAdapter()
        recyclerView.adapter = adapter
        adapter.submitList(Database.getMateriali())

        return view
    }
}

class MaterialeRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var items : ArrayList<Materiale>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = MaterialeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_home, parent, false))
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is MaterialeViewHolder -> {
                holder.bind(items.get(position))
                holder.itemView.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToMaterialeFragment(holder.materiale)
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(list : ArrayList<Materiale>) {
        items = list
    }

    class MaterialeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        lateinit var materiale : Materiale

        fun bind(materiale: Materiale) {
            this.materiale = materiale
            val iv_foto_materiale = itemView.findViewById<ImageView>(R.id.iv_foto_materiale)
            //TODO(Mettere immagine presa dalla classe)
            val tv_nome_materiale = itemView.findViewById<TextView>(R.id.tv_nome_materiale)
            tv_nome_materiale.setText(materiale.nome)
            val tv_nome_venditore = itemView.findViewById<TextView>(R.id.tv_nome_venditore)
            tv_nome_venditore.setText(materiale.proprietario)
            val tv_prezzo = itemView.findViewById<TextView>(R.id.tv_prezzo)
            tv_prezzo.setText(materiale.prezzo.toString())
        }
    }
}
