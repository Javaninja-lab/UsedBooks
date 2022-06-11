package com.example.usedbooks.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import com.example.usedbooks.main.home.HomeFragmentDirections
import com.example.usedbooks.main.profile.ProfileFragmentDirections
import com.example.usedbooks.main.search.SearchFragmentDirections
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class MaterialeRecyclerAdapter(string: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var items : ArrayList<Materiale>
    private var fragment = string

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = MaterialeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_material_complete, parent, false))
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is MaterialeViewHolder -> {
                holder.bind(items.get(position))
                holder.itemView.setOnClickListener {
                    if(fragment == "home") {
                        val action = HomeFragmentDirections.actionHomeFragmentToMaterialeFragment(holder.materiale)
                        it.findNavController().navigate(action)
                    } else if(fragment == "search") {
                        val action = SearchFragmentDirections.actionSearchFragmentToMaterialeFragment(holder.materiale)
                        it.findNavController().navigate(action)
                    } else if(fragment == "profile") {
                        val action = ProfileFragmentDirections.actionProfileFragmentToMaterialeFragment(holder.materiale)
                        it.findNavController().navigate(action)
                    }
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
            thread(start = true) {
                val photoBitmap : Bitmap = Database.getPhotoMateriale(materiale.photos[0])
                iv_foto_materiale.postDelayed({
                    iv_foto_materiale.setImageBitmap(photoBitmap)
                }, 1000L)
                }


            //TODO(Mettere immagine presa dalla classe)
            val tv_nome_materiale = itemView.findViewById<TextView>(R.id.tv_nome_materiale)
            tv_nome_materiale.setText(materiale.nome)
            val tv_nome_venditore = itemView.findViewById<TextView>(R.id.tv_nome_venditore)
            tv_nome_venditore.setText(materiale.proprietario)
            val tv_prezzo = itemView.findViewById<TextView>(R.id.tv_prezzo)
            tv_prezzo.setText(materiale.prezzo.toString())
            val tv_state_material = itemView.findViewById<TextView>(R.id.tv_state_material)
            tv_state_material.setText(materiale.stato)
        }
    }
}
