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
import com.example.usedbooks.dataClass.User

class UserAdapter(val context: Context?, val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mittente= itemView.findViewById<TextView>(R.id.tv_nome_studente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_chat,parent,false)// da vedere se funziona
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser= userList[position]
        holder.mittente.text= currentUser.username
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}