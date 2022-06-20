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
import com.example.usedbooks.adapters.UserAdapter
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
        userList = ArrayList()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        adapter = UserAdapter(userList)

        userRecyclerView = view.findViewById(R.id.rv_chats)
        userRecyclerView.layoutManager = LinearLayoutManager(view.context)
        userRecyclerView.adapter = adapter

        Database.setUsersChat(userList, adapter, view)

        return view
    }



}