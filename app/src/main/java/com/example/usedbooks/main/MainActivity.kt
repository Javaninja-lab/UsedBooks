package com.example.usedbooks.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.usedbooks.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.fragmentContainerViewMainActivity)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)


        val btn_Add_Annuncio = findViewById<FloatingActionButton>(R.id.btn_Add_Annuncio)
        btn_Add_Annuncio.setOnClickListener {
            val i = Intent(this, NuovoAnnuncioActivity::class.java)
            startActivity(i)
        }
    }
}