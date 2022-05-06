package com.example.usedbooks.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Database.inizializateDatabase()
    }
}