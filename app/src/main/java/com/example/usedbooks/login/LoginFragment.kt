package com.example.usedbooks.login

import android.content.ContentValues
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Gestore
import com.example.usedbooks.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    companion object {
        var fatto : Boolean = false
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_login, container, false)

        auth = FirebaseAuth.getInstance()

        val btn_SingIn = layout.findViewById<Button>(R.id.btn_SingIn)
        btn_SingIn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        val btn_Login = layout.findViewById<Button>(R.id.btn_Login)
        btn_Login.setOnClickListener {
            onLoginClick()
        }

        return layout
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            login(currentUser.email!!)
        }
    }

    private fun onLoginClick() {
        val emailEditText = requireView().findViewById<EditText>(R.id.et_email_login)
        val passwordEditText = requireView().findViewById<EditText>(R.id.et_password_login)
        val email = emailEditText.text.toString().trim()
        val passwordNotHashed = passwordEditText.text.toString().trim()
        if (email.isEmpty()) {
            emailEditText.error = "Enter email"
            return
        }
        if (passwordNotHashed.isEmpty()) {
            passwordEditText.error = "Enter password"
            return
        }
        val password = Gestore.getHash(passwordNotHashed)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful) {
                login(email)
            } else {
                Toast.makeText(emailEditText.context, "Credenziali errate", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun login(email : String) {
        Database.setLoggedStudent(Database.getStudente(email)!!)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}