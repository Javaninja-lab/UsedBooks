package com.example.usedbooks.login

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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {

    private lateinit var mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater =  inflater.inflate(R.layout.fragment_register, container, false)

        val btn= layoutInflater.findViewById<Button>(R.id.btn_Register)
        btn.setOnClickListener {
            val name= layoutInflater.findViewById<EditText>(R.id.et_Name)
            val surname= layoutInflater.findViewById<EditText>(R.id.et_Surname)
            val uni= layoutInflater.findViewById<EditText>(R.id.et_University)
            val email= layoutInflater.findViewById<EditText>(R.id.et_email_login)
            val password= layoutInflater.findViewById<EditText>(R.id.et_Password)
            val password2= layoutInflater.findViewById<EditText>(R.id.et_ConfirmPassword)

            if(password.text.toString().equals(password2.text.toString())) {
                val studente = Database.addStudente(
                    name.text.toString(),
                    surname.text.toString(),
                    email.text.toString(),
                    Gestore.getHash(password.text.toString()))
                if(studente != null) {
                    Toast.makeText(layoutInflater.context, R.string.register_ok, Toast.LENGTH_LONG).show()
                    val studente = Database.getStudente(email.text.toString())
                    Database.setLoggedStudent(studente!!)
                    val s = Database.getStudente(email.text.toString())
                    addUserToDatabaseRealtime(s?.id.toString(),name.text.toString()+" "+surname.text.toString())
                    Navigation.findNavController(it).navigate(R.id.action_registerFragment_to_mainActivity)
                } else {
                    Toast.makeText(layoutInflater.context, R.string.register_not_ok, Toast.LENGTH_LONG).show()
                }
            }
            else
                Toast.makeText(layoutInflater.context, R.string.password_confirmpassword_different, Toast.LENGTH_SHORT).show()
        }

        return layoutInflater
    }

    private fun addUserToDatabaseRealtime(id:String,nome:String){
        mDbRef= FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(id).setValue(User(id,nome))
    }


}