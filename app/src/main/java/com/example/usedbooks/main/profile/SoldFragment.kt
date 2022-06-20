package com.example.usedbooks.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import com.example.usedbooks.dataClass.User
import com.example.usedbooks.adapters.UserAdapter
import com.example.usedbooks.adapters.UserSaleAdapter
import com.google.firebase.database.*

class SoldFragment : Fragment() {

    val args : SoldFragmentArgs by navArgs()
    lateinit var materiale: Materiale

    private lateinit var  userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserSaleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_sold, container, false)

        materiale = args.materiale

        userList = ArrayList()
        adapter = UserSaleAdapter(userList, materiale)

        userRecyclerView = layout.findViewById(R.id.rv_chat_sold)
        userRecyclerView.layoutManager= LinearLayoutManager(this.context)
        userRecyclerView.adapter=adapter

        Database.setUsersChat(userList, adapter)

        return layout
    }
}