package com.example.usedbooks.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.inizializateDatabase()
        mAuth= FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_login, container, false)

        val btn_SingIn = layout.findViewById<Button>(R.id.btn_SingIn)
        btn_SingIn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        val btn_Login = layout.findViewById<Button>(R.id.btn_Login)
        btn_Login.setOnClickListener {
            val email : String = layout.findViewById<EditText>(R.id.et_email_login).text.toString()
            val password = layout.findViewById<EditText>(R.id.et_password_login).text.toString()
            val studente = Database.getStudente(email)
            if(studente==null) {
                Log.d(ContentValues.TAG,"STUDENTE NON TROVATO")
            }
            else {
                Log.d(ContentValues.TAG,"STUDENTE trovato ${studente.nome}")
                if(password.equals(studente.password)) {
                    Database.setLoggedStudent(studente)
                    Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_mainActivity)
                }
                else {
                    Log.d(ContentValues.TAG,"password errata ${studente.nome}")
                }
            }
        }
        return layout
    }
}