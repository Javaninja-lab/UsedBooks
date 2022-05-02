package com.example.usedbooks.main.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.KeyListener
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Messaggio
import com.example.usedbooks.firebase.MessageAdapter
import com.google.firebase.database.*

class TextChat : AppCompatActivity() {
    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_chat)


        val name = intent.getStringExtra("name")
        val receiverid =intent.getStringExtra("id")
        val senderid= Database.getLoggedStudent().id

        val mDbref= FirebaseDatabase.getInstance().getReference()
         //todo impostare una text view per il nome


        //codice univoco della stanza
        val senderRoom= receiverid+senderid
        val receiverRoom= senderid+receiverid


        val chatRecyclerView= findViewById<RecyclerView>(R.id.RelativeChat)
        val messageBox= findViewById<EditText>(R.id.mb_messaggio)
        val sendButton= findViewById<Button>(R.id.invioButton)
        val messageList= ArrayList<Messaggio>()
        val messageAdapter= MessageAdapter(this,messageList)

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
        //TODO(aggiungere il messaggio al database)
        sendButton.setOnClickListener{
            val message= messageBox.text.toString()
            val messageObject = Messaggio(message, senderid)
            mDbref.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbref.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }

        val mb_messaggio = findViewById<TextView>(R.id.mb_messaggio)
        mb_messaggio.keyListener
    }
}