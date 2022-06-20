package com.example.usedbooks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Gestore
import com.example.usedbooks.dataClass.User
import com.example.usedbooks.main.chat.ListChatsFragmentDirections

class UserAdapter(val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_nome_studente = itemView.findViewById<TextView>(R.id.tv_nome_studente)
        val iv_foto_profilo = itemView.findViewById<ImageView>(R.id.iv_foto_profilo)
        val tv_ultimo_messaggio = itemView.findViewById<TextView>(R.id.tv_ultimo_messaggio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_chat,parent,false)
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        Gestore.bindUserAdapter(position, holder, userList) {
            val action = ListChatsFragmentDirections.actionChatFragmentToTextChat(userList[position])
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}