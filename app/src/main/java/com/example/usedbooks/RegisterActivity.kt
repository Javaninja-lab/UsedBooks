package com.example.usedbooks

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val db= Firebase.firestore
        val btn= findViewById<Button>(R.id.btn_Register)
        btn.setOnClickListener {
            val name= findViewById<EditText>(R.id.et_Name)
            val surname= findViewById<EditText>(R.id.et_Surname)
            val uni= findViewById<EditText>(R.id.et_University)
            val email= findViewById<EditText>(R.id.et_email)
            val password= findViewById<EditText>(R.id.et_Password)
            val password2= findViewById<EditText>(R.id.et_ConfirmPassword)

            Log.d("test","test")
            val studente = hashMapOf(
                "cognome" to surname.text.toString(),
                "nome" to name.text.toString(),
                "email" to email.text.toString(),
                "password" to password.text.toString()
            )
            db.collection("studenti")
                .add(studente)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}
