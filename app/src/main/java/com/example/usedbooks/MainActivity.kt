package com.example.usedbooks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val chatFragment = ChatFragment()
    private val profileFragment = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerViewMainActivity, homeFragment).addToBackStack(homeFragment.tag).commit()

        bottomNavigationView.setOnItemSelectedListener {
            var itemSelected: Fragment = homeFragment
            when (it.itemId) {
                R.id.home -> {
                    itemSelected = homeFragment
                }
                R.id.search -> {
                    itemSelected = searchFragment
                }
                R.id.chat -> {
                    itemSelected = chatFragment
                }
                R.id.profile -> {
                    itemSelected = profileFragment
                }
            }
            if(supportFragmentManager.fragments.last().id != itemSelected.id){
                if(supportFragmentManager.fragments.contains(itemSelected)){
                    val lastFragment = supportFragmentManager.fragments.last()
                    for(i in 0..supportFragmentManager.backStackEntryCount){
                        supportFragmentManager.popBackStack()
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewMainActivity, lastFragment).addToBackStack(null).commit()
                }
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewMainActivity, itemSelected).addToBackStack(null).commit()
            }
            true
        }
        buttonAddAnnuncio.setOnClickListener {
            val i = Intent(this,NuovoAnnuncioActivity::class.java)
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        /*if(supportFragmentManager.backStackEntryCount > 0) {
            when(supportFragmentManager.fragments.last().tag){
                "home" -> {
                    bottomNavigationView.selectedItemId = R.id.home
                }
                "search" -> {
                    bottomNavigationView.selectedItemId = R.id.search
                }
                "chat" -> {
                    bottomNavigationView.selectedItemId = R.id.chat
                }
                "profile" -> {
                    bottomNavigationView.selectedItemId = R.id.profile
                }
            }
        }*/
        super.onBackPressed()
    }
}