package com.example.usedbooks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.*

class UserSaleAdapter (private val userList: ArrayList<User>, private val materiale: Materiale) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_chat,parent,false)// da vedere se funziona
        return UserAdapter.UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val currentUser = userList[position]
        Gestore.bindUserAdapter(position, holder, userList) {
            holder.itemView.setOnClickListener {
                if(currentUser.id!=null) {
                    Database.registerTransaction(currentUser.id, materiale)
                    it.findNavController().navigate(R.id.action_soldFragment_to_profileFragment)
                }
                else {
                    Toast.makeText(it.context, it.context.getText(R.string.error), Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}

