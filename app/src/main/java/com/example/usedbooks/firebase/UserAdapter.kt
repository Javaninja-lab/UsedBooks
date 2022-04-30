package com.example.usedbooks.firebase

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Studente

class UserAdapter(val context: Context, val userList: ArrayList<Studente>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mittente= itemView.findViewById<TextView>(R.id.tv_nome_studente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_chat,parent,false)
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser= userList[position]
        holder.mittente.text= currentUser.nome+" "+currentUser.cognome
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}