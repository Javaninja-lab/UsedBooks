package com.example.usedbooks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.usedbooks.dataClass.Database

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btn= findViewById<Button>(R.id.btn_Register)
        btn.setOnClickListener {
            val name= findViewById<EditText>(R.id.et_Name)
            val surname= findViewById<EditText>(R.id.et_Surname)
            val uni= findViewById<EditText>(R.id.et_University)
            val email= findViewById<EditText>(R.id.et_email)
            val password= findViewById<EditText>(R.id.et_Password)
            val password2= findViewById<EditText>(R.id.et_ConfirmPassword)

            if(password.text.toString().equals(password2.text.toString()))
            {
                Database.addStudente(name.text.toString(),surname.text.toString(),email.text.toString(),password.text.toString(),)
            }
            else
            {
                Toast.makeText(this, "password non uguali", Toast.LENGTH_SHORT).show()
            }



        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}
