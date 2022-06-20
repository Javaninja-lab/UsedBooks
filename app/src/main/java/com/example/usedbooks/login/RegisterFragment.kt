package com.example.usedbooks.login

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Gestore
import com.example.usedbooks.dataClass.User
import com.example.usedbooks.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        auth = FirebaseAuth.getInstance()

        val btn = view.findViewById<Button>(R.id.btn_Register)
        btn.setOnClickListener {
            onSingUpClick(view)
        }

        return view
    }

    private fun onSingUpClick(view : View){
        val name = view.findViewById<EditText>(R.id.et_Name).text.toString().trim()
        val surname = view.findViewById<EditText>(R.id.et_Surname).text.toString().trim()
        val email = view.findViewById<EditText>(R.id.et_email_login).text.toString().trim()
        val et_Password = view.findViewById<EditText>(R.id.et_Password)
        val et_ConfirmPassword = view.findViewById<EditText>(R.id.et_ConfirmPassword)
        val password = Gestore.getHash(et_Password.text.toString().trim())
        val password2 = Gestore.getHash(et_ConfirmPassword.text.toString().trim())

        if(password.equals(password2)) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful) {
                    Database.addStudente(name, surname, email, password)
                    Toast.makeText(layoutInflater.context, R.string.register_ok, Toast.LENGTH_LONG).show()
                    activity?.onBackPressed()
                }
                else {
                    Toast.makeText(layoutInflater.context, R.string.register_not_ok, Toast.LENGTH_LONG).show()
                    Toast.makeText(layoutInflater.context, it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
        else {
            val msg = getString(R.string.password_confirmpassword_different)
            Toast.makeText(
                layoutInflater.context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
            et_Password.error = msg
            et_ConfirmPassword.error = msg
        }
    }
}