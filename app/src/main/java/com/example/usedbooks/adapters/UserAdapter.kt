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
import com.example.usedbooks.dataClass.User
import com.example.usedbooks.main.chat.ListChatsFragmentDirections

class UserAdapter(val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_nome_studente = itemView.findViewById<TextView>(R.id.tv_nome_studente)
        val iv_foto_profilo = itemView.findViewById<ImageView>(R.id.iv_foto_profilo)
        val tv_ultimo_messaggio = itemView.findViewById<TextView>(R.id.tv_ultimo_messaggio)
        val tv_data_messaggio = itemView.findViewById<TextView>(R.id.tv_data_messaggio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_chat,parent,false)// da vedere se funziona
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.tv_nome_studente.text = currentUser.username


        if(currentUser.id != null) {
            val uriImageStudent = Database.getUriPhotosStudente(currentUser.id)
            if (!uriImageStudent.equals(""))
                holder.iv_foto_profilo.setImageBitmap(Database.getPhotoStudente(uriImageStudent))
            else {
                holder.iv_foto_profilo.setImageResource(R.drawable.placeholder)
            }
        }
        else {
            holder.iv_foto_profilo.setImageResource(R.drawable.placeholder)
        }

         val messaggio = Database.getLastMessage(currentUser)
        //TODO("Aggiungere data al messaggio")
        holder.tv_data_messaggio.text = messaggio.message
        holder.tv_ultimo_messaggio.text = messaggio.message
        holder.itemView.setOnClickListener {
            val action = ListChatsFragmentDirections.actionChatFragmentToTextChat(currentUser.username!!, currentUser.id!!)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}