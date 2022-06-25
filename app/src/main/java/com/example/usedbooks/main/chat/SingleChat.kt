package com.example.usedbooks.main.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Messaggio
import com.example.usedbooks.dataClass.User
import com.example.usedbooks.adapters.MessageAdapter
import com.example.usedbooks.dataClass.Gestore
import com.google.firebase.database.*

class SingleChat : AppCompatActivity() {

    private val args : SingleChatArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_chat)


        val vendor = args.vendor
        val name = vendor.username
        val receiverid = vendor.id
        val senderid= Database.getLoggedStudent().id

        val mDbref= FirebaseDatabase.getInstance().reference

        val tv_nome_contatto = findViewById<TextView>(R.id.tv_nome_contatto)
        tv_nome_contatto.text = "${tv_nome_contatto.text}: $name"
        val iv_foto_profilo = findViewById<ImageView>(R.id.iv_foto_profilo)
        Gestore.getProfileImage(iv_foto_profilo, receiverid!!)
        iv_foto_profilo.clipToOutline = true

        val senderRoom= receiverid+senderid
        val receiverRoom= senderid+receiverid

        val chatRecyclerView = findViewById<RecyclerView>(R.id.RelativeChat)
        val messageBox = findViewById<EditText>(R.id.mb_messaggio)
        val sendButton = findViewById<Button>(R.id.invioButton)
        val messageList = ArrayList<Messaggio>()
        val messageAdapter = MessageAdapter(this,messageList)

        chatRecyclerView.layoutManager= LinearLayoutManager(this)
        chatRecyclerView.adapter= messageAdapter

        //aggiungere i messaggi alla recyclervieew
        mDbref.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message= postSnapshot.getValue(Messaggio::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                    chatRecyclerView.scrollToPosition(messageList.size-1)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendButton.setOnClickListener{
            val message= messageBox.text.toString().trim()
            if(message.isEmpty()){
                messageBox.error="Inserisci un messaggio"
            }
            else {
                val messageObject = Messaggio(message, senderid)
                mDbref.child("chats").child(senderRoom).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbref.child("chats").child(receiverRoom).child("messages").push()
                            .setValue(messageObject)
                    }

                mDbref.child("users").child(receiverid).child(senderid)
                    .setValue(User(senderid, Database.getLoggedStudent().nome))
                messageBox.setText("")
            }
        }


    }
}