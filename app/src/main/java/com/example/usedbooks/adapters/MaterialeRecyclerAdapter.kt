package com.example.usedbooks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.customView.PersonalProgressBar
import com.example.usedbooks.dataClass.Gestore
import com.example.usedbooks.dataClass.Materiale
import com.example.usedbooks.main.home.HomeFragmentDirections
import com.example.usedbooks.main.profile.ProfileFragmentDirections
import com.example.usedbooks.main.search.SearchFragmentDirections
import kotlin.concurrent.thread

open class MaterialeRecyclerAdapter(private val fragment: String, private val button : Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var items : ArrayList<Materiale>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView : View
        val view : RecyclerView.ViewHolder = if(button){
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_material_with_button, parent, false)
            MaterialeButtonViewHolder(itemView)
        } else {
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_material_complete, parent, false)
            MaterialeViewHolder(itemView)
        }
        val pb_image = PersonalProgressBar(parent.context, itemView.findViewById(R.id.cl_image), "pb_image", 150)
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
            is MaterialeButtonViewHolder -> {
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
            val pb_image = itemView.findViewWithTag<PersonalProgressBar>("pb_image")
            pb_image.caricamento {
                Gestore.setBitmap(materiale, iv_foto_materiale, false)
                itemView.post {
                    pb_image.visibility = View.GONE
                    iv_foto_materiale.visibility = View.VISIBLE
                }
            }
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

    class MaterialeButtonViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        lateinit var materiale : Materiale

        fun bind(materiale: Materiale) {
            this.materiale = materiale
            val iv_foto_materiale = itemView.findViewById<ImageView>(R.id.iv_foto_materiale)
            thread(start = true) {
                Gestore.setBitmap(materiale, iv_foto_materiale, false)
                itemView.post {
                    itemView.findViewWithTag<PersonalProgressBar>("pb_image").visibility = View.GONE
                    iv_foto_materiale.visibility = View.VISIBLE
                }
            }
            val tv_nome_materiale = itemView.findViewById<TextView>(R.id.tv_nome_materiale)
            tv_nome_materiale.setText(materiale.nome)
            val tv_prezzo = itemView.findViewById<TextView>(R.id.tv_prezzo)
            tv_prezzo.setText(materiale.prezzo.toString())
            val btn_sold = itemView.findViewById<Button>(R.id.btn_sold)
            btn_sold.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToSoldFragment(materiale)
                it.findNavController().navigate(action)
            }
        }
    }
}
