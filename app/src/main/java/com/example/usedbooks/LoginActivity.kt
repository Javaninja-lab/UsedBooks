package com.example.usedbooks

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Gestore
import com.example.usedbooks.dataClass.Studente
import com.example.usedbooks.main.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Database.inizializateDatabase()

        val btn_SingIn = findViewById<Button>(R.id.btn_SingIn)
        btn_SingIn.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java);
            startActivity(i)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        val btn_Login = findViewById<Button>(R.id.btn_Login)
        btn_Login.setOnClickListener {

            val email : String = findViewById<EditText>(R.id.et_email_login).text.toString()
            val password = findViewById<EditText>(R.id.et_password_login).text.toString()
            val studente = Database.getStudente(email)
            if(studente==null)
            {
                Log.d(TAG,"STUDENTE NON TROVATO")
            }
            else
            {
                Log.d(TAG,"STUDENTE trovato ${studente.nome}")
                if(password.equals(studente.password))
                {
                    Database.setLoggedStudent(studente)
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }
                else
                {
                    Log.d(TAG,"password errata ${studente.nome}")
                }
            }

            /*if(corretto != null) {

            }*/
        }
    }
}