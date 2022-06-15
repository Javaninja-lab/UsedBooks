package com.example.usedbooks.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Messaggio
import com.example.usedbooks.dataClass.User
import com.example.usedbooks.main.chat.ListChatsFragmentDirections

class UserAdapter(val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mittente = itemView.findViewById<TextView>(R.id.tv_nome_studente)
        val fotoProfilo = itemView.findViewById<ImageView>(R.id.iv_foto_profilo)
        val utlimoMessaggio = itemView.findViewById<TextView>(R.id.tv_ultimo_messaggio)
        val dataMessaggio = itemView.findViewById<TextView>(R.id.tv_data_messaggio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_chat,parent,false)// da vedere se funziona
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.mittente.text = currentUser.username

        //holder.fotoProfilo.setImageBitmap()
        //TODO("Asseganre immagine alla chat")
         val messaggio = Messaggio("ciao","1")  //Database.getLastMessage(currentUser)
        //TODO("Aggiungere data al messaggio")
        holder.dataMessaggio.text = messaggio.message
        holder.utlimoMessaggio.text = messaggio.message  //messaggio.message?.subSequence(0..32)
        holder.itemView.setOnClickListener {
            val action = ListChatsFragmentDirections.actionChatFragmentToTextChat(currentUser.username!!, currentUser.id!!)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}