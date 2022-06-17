package com.example.usedbooks.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Messaggio

class MessageAdapter (val context:Context,val messageList: ArrayList<Messaggio>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == 1) {
                val view: View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)// da vedere se funziona
                return ReceiveViewHolder(view)
            } else {
                val view: View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)// da vedere se funziona
                return SentViewHolder(view)
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass== SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text= currentMessage.message
        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text=currentMessage.message
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int) : Int {
        val currentMessage=messageList[position]
        return if (Database.getLoggedStudent().id == currentMessage.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }


    class SentViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val sentMessage= itemView.findViewById<TextView>(R.id.txt_sent_message)
    }
    class ReceiveViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val receiveMessage= itemView.findViewById<TextView>(R.id.txt_receive_message)
    }

}