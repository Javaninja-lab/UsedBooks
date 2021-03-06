package com.example.usedbooks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.customView.PersonalProgressBar
import com.example.usedbooks.dataClass.Gestore
import com.example.usedbooks.dataClass.Materiale
import com.example.usedbooks.main.home.HomeFragmentDirections
import com.example.usedbooks.main.profile.ProfileFragmentDirections
import com.example.usedbooks.main.search.SearchFragmentDirections

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
        PersonalProgressBar(parent.context, itemView.findViewById(R.id.cl_image),150,"pb_image")
        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is MaterialeViewHolder -> {
                holder.bind(items[position])
                holder.itemView.setOnClickListener {
                    when (fragment) {
                        "home" -> {
                            val action = HomeFragmentDirections.actionHomeFragmentToMaterialeFragment(holder.materiale)
                            it.findNavController().navigate(action)
                        }
                        "search" -> {
                            val action = SearchFragmentDirections.actionSearchFragmentToMaterialeFragment(holder.materiale)
                            it.findNavController().navigate(action)
                        }
                        "profile" -> {
                            val action = ProfileFragmentDirections.actionProfileFragmentToMaterialeFragment(holder.materiale)
                            it.findNavController().navigate(action)
                        }
                    }
                }
            }
            is MaterialeButtonViewHolder -> {
                holder.bind(items[position])
                holder.itemView.setOnClickListener {
                    when (fragment) {
                        "home" -> {
                            val action = HomeFragmentDirections.actionHomeFragmentToMaterialeFragment(holder.materiale)
                            it.findNavController().navigate(action)
                        }
                        "search" -> {
                            val action = SearchFragmentDirections.actionSearchFragmentToMaterialeFragment(holder.materiale)
                            it.findNavController().navigate(action)
                        }
                        "profile" -> {
                            val action = ProfileFragmentDirections.actionProfileFragmentToMaterialeFragment(holder.materiale)
                            it.findNavController().navigate(action)
                        }
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
            Gestore.bindMaterialViewHolder(materiale, itemView, false)
        }
    }

    class MaterialeButtonViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        lateinit var materiale : Materiale

        fun bind(materiale: Materiale) {
            this.materiale = materiale
            Gestore.bindMaterialViewHolder(materiale, itemView, true)
        }
    }
}
