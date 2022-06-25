package com.example.usedbooks.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Gestore
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        auth = FirebaseAuth.getInstance()

        val btn_Register = view.findViewById<Button>(R.id.btn_Register)
        btn_Register.setOnClickListener {
            btn_Register.isEnabled = false
            onSingUpClick(view)
            btn_Register.isEnabled = true
        }

        return view
    }

    private fun onSingUpClick(view : View){
        var error = false
        val et_Name = view.findViewById<EditText>(R.id.et_Name)
        val name = et_Name.text.toString().trim()
        if(name.isEmpty()) {
            et_Name.error = view.context.getString(R.string.empty)
            error = true
        }
        val et_Surname = view.findViewById<EditText>(R.id.et_Surname)
        val surname = et_Surname.text.toString().trim()
        if(surname.isEmpty()) {
            et_Surname.error = view.context.getString(R.string.empty)
            error = true
        }
        val et_email_login = view.findViewById<EditText>(R.id.et_email_login)
        val email = et_email_login.text.toString().trim()
        if(email.isEmpty()) {
            et_email_login.error = view.context.getString(R.string.empty)
            error = true
        }
        val et_Password = view.findViewById<EditText>(R.id.et_Password)
        val passwordNotHashed = et_Password.text.toString().trim()
        if(passwordNotHashed.isEmpty()) {
            et_Password.error = view.context.getString(R.string.empty)
            error = true
        }
        val password = Gestore.getHash(passwordNotHashed)
        val et_ConfirmPassword = view.findViewById<EditText>(R.id.et_ConfirmPassword)
        val passwordNotHashed2 = et_ConfirmPassword.text.toString().trim()
        if(passwordNotHashed2.isEmpty()) {
            et_ConfirmPassword.error = view.context.getString(R.string.empty)
            error = true
        }
        val password2 = Gestore.getHash(passwordNotHashed2)
        if(!error) {
            if (password == password2) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Database.addStudente(name, surname, email, password)
                        Toast.makeText(
                            layoutInflater.context,
                            R.string.register_ok,
                            Toast.LENGTH_LONG
                        ).show()
                        activity?.onBackPressed()
                    } else {
                        Toast.makeText(
                            layoutInflater.context,
                            "${R.string.register_not_ok}, ${it.exception!!.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                val msg = getString(R.string.password_confirmpassword_different)
                Toast.makeText(
                    layoutInflater.context,
                    msg,
                    Toast.LENGTH_SHORT
                ).show()
                et_Password.error = msg
                et_ConfirmPassword.error = msg
            }
        } else {
            val msg = view.context.getString(R.string.message_empty)
            Toast.makeText(
                layoutInflater.context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}