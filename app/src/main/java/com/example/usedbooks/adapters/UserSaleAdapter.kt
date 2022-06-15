package com.example.usedbooks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import com.example.usedbooks.dataClass.Messaggio
import com.example.usedbooks.dataClass.User
import com.example.usedbooks.main.chat.ListChatsFragmentDirections
import com.example.usedbooks.main.profile.SoldFragmentDirections

class UserSaleAdapter (private val userList: ArrayList<User>, private val materiale: Materiale) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_chat,parent,false)// da vedere se funziona
        return UserAdapter.UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.mittente.text = currentUser.username

        //holder.fotoProfilo.setImageBitmap()
        //TODO("Asseganre immagine alla chat")
        val messaggio = Messaggio("ciao","1")  //Database.getLastMessage(currentUser)
        //TODO("Aggiungere data al messaggio")
        holder.dataMessaggio.text = messaggio.message
        holder.utlimoMessaggio.text = messaggio.message  //messaggio.message?.subSequence(0..32)
        holder.itemView.setOnClickListener {
            if(currentUser.id!=null) {
                Database.registerTransaction(currentUser.id, materiale)
                it.findNavController().navigate(R.id.action_soldFragment_to_profileFragment)
            }
            else {
                Toast.makeText(it.context, "Errore", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
