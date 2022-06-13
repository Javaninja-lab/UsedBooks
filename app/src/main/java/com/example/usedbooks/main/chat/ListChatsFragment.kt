package com.example.usedbooks.main.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.User
import com.example.usedbooks.firebase.UserAdapter
import com.google.firebase.database.*

class ListChatsFragment : Fragment() {

    private lateinit var  userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mDbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        userList= ArrayList()
        mDbRef= FirebaseDatabase.getInstance().getReference()
        adapter = UserAdapter(this.context,userList)

        userRecyclerView = view.findViewById(R.id.RecyclerViewChat)
        userRecyclerView.layoutManager= LinearLayoutManager(this.context)
        userRecyclerView.adapter=adapter

        //prelevo dati dal database realtime

        mDbRef.child("users").child(Database.getLoggedStudent().id).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser =postSnapshot.getValue(User::class.java)
                    if(Database.getLoggedStudent()!!.id != currentUser?.id) {
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
        return view
    }



}