package com.example.usedbooks.main

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.adapters.MaterialeRecyclerAdapter
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import com.example.usedbooks.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val iv_foto_profilo = view.findViewById<ImageView>(R.id.iv_foto_profilo)
        //TODO(Cambiamento immagine profilo)

        val tv_nome_cliente = view.findViewById<TextView>(R.id.tv_nome_cliente)
        tv_nome_cliente.text = "${getString(R.string.hi)}, ${Database.getLoggedStudent().nome}"

        val recyclerView  = view.findViewById<RecyclerView>(R.id.lv_profile)
        val tv_no_material = view.findViewById<TextView>(R.id.tv_no_material)
        val adapter = MaterialeRecyclerAdapter()
        recyclerView.adapter = adapter
        val array = ArrayList<Materiale>()
        for (m in Database.getMaterialiStudente(Database.getLoggedStudent().id)){
            if(m!=null)
                array.add(m)
        }
        if(array.isEmpty()){
            recyclerView.visibility = View.GONE
            tv_no_material.visibility = View.VISIBLE
        } else
            adapter.submitList(array)

        val ib_logout = view.findViewById<ImageButton>(R.id.ib_logout)
        ib_logout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        return view
    }

}