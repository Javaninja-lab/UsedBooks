package com.example.usedbooks.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Messaggio
import com.example.usedbooks.firebase.MessageAdapter
import com.google.firebase.database.*

class TextChat : AppCompatActivity() {

    private lateinit var chatRecyclerView : RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Messaggio>
    private lateinit var mDbref : DatabaseReference


    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_chat)


        val name = intent.getStringExtra("name")
        val receiverid =intent.getStringExtra("id")
        val senderid= Database.getLoggedStudent().id

        mDbref= FirebaseDatabase.getInstance().getReference()
         //todo impostare una text view per il nome


        //codice univoco della stanza
        senderRoom= receiverid+senderid
        receiverRoom= senderid+receiverid


        chatRecyclerView= findViewById(R.id.RelativeChat)
        messageBox= findViewById(R.id.messageBox)
        sendButton= findViewById(R.id.invioButton)
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)

        chatRecyclerView.layoutManager= LinearLayoutManager(this)
        chatRecyclerView.adapter= messageAdapter

        //aggiungere i messaggi alla recyclervieew
        mDbref.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message= postSnapshot.getValue(Messaggio::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })



        //aggiungere il messaggio al database
        sendButton.setOnClickListener{

            val message= messageBox.text.toString()
            val messageObject = Messaggio(message, senderid)
            mDbref.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbref.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")


        }

    }
}