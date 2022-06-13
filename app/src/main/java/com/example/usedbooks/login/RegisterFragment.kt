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
    private lateinit var mDbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_register, container, false)

        auth = FirebaseAuth.getInstance()

        val btn = layoutInflater.findViewById<Button>(R.id.btn_Register)
        btn.setOnClickListener {
            onSingUpClick()
        }

        return layoutInflater
    }

    private fun addUserToDatabaseRealtime(id:String,nome:String){
        mDbRef= FirebaseDatabase.getInstance().getReference()
        mDbRef.child("users").child(id).setValue(User(id,nome))
    }

    private fun onSingUpClick() {
        val name = view?.findViewById<EditText>(R.id.et_Name)!!.text.toString().trim()
        val surname = view?.findViewById<EditText>(R.id.et_Surname)!!.text.toString().trim()
        val uni = view?.findViewById<EditText>(R.id.et_University)!!.text.toString().trim()
        val email = view?.findViewById<EditText>(R.id.et_email_login)!!.text.toString().trim()
        val password = Gestore.getHash(view?.findViewById<EditText>(R.id.et_Password)!!.text.toString().trim())
        val password2 = Gestore.getHash(view?.findViewById<EditText>(R.id.et_ConfirmPassword)!!.text.toString().trim())

        if(password.equals(password2)) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful) {
                    val studente = Database.addStudente(name, surname, email, password)
                    Database.setLoggedStudent(studente!!)
                    addUserToDatabaseRealtime(studente.id, "$name $surname")
                    Toast.makeText(layoutInflater.context, R.string.register_ok, Toast.LENGTH_LONG).show()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else {
                    Toast.makeText(layoutInflater.context, R.string.register_not_ok, Toast.LENGTH_LONG).show()
                    Toast.makeText(layoutInflater.context, it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
        else
            Toast.makeText(layoutInflater.context, R.string.password_confirmpassword_different, Toast.LENGTH_SHORT).show()
    }
}