package com.example.usedbooks.main.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.KeyListener
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Messaggio
import com.example.usedbooks.firebase.MessageAdapter
import com.example.usedbooks.main.home.MaterialeFragmentArgs
import com.google.firebase.database.*

class TextChat : AppCompatActivity() {

    val args : TextChatArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_chat)


        val name : String = args.nameVendor
        val receiverid : String = args.id

        val senderid= Database.getLoggedStudent()!!.id

        val mDbref= FirebaseDatabase.getInstance().getReference()

        val tv_nome_contatto = findViewById<TextView>(R.id.tv_nome_contatto)
        tv_nome_contatto.setText(name)

        //codice univoco della stanza
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
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendButton.setOnClickListener{
            val message= messageBox.text.toString().trim()
            val messageObject = Messaggio(message, senderid)
            mDbref.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbref.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }


    }
}