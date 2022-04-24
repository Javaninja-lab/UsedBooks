package com.example.usedbooks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Studente
import com.example.usedbooks.main.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Database.inizializateDatabase()
        val test : MutableList<Studente> = Database.getStudenti()

        val btn_SingIn = findViewById<Button>(R.id.btn_SingIn)
        btn_SingIn.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java);
            startActivity(i)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        val btn_Login = findViewById<Button>(R.id.btn_Login)
        btn_Login.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            //overridePendingTransition(androidx.appcompat.R.anim.abc_slide_out_bottom, androidx.appcompat.R.anim.abc_slide_in_top)
        }
    }
}