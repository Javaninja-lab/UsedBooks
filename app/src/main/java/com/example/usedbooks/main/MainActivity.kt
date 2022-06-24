package com.example.usedbooks.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.usedbooks.R
import com.example.usedbooks.main.nuovoAnnuncio.NuovoAnnuncioActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fg_main) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)


        val btn_Add_Annuncio = findViewById<FloatingActionButton>(R.id.btn_Add_Annuncio)
        btn_Add_Annuncio.setOnClickListener {
            val i = Intent(this, NuovoAnnuncioActivity::class.java)
            startActivity(i)
        }
    }


}