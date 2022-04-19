package com.example.usedbooks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerViewMainActivity, HomeFragment()).commit()
        bottomAppBar.setOnMenuItemClickListener {
            var itemSelected : Fragment = HomeFragment()
            when(it.itemId) {
                R.id.home -> {
                    itemSelected = HomeFragment()
                }
                R.id.search -> {
                    itemSelected = SearchFragment()
                }
                R.id.add -> {
                    itemSelected = HomeFragment()
                }
                R.id.chat -> {
                    itemSelected = ChatFragment()
                }
                R.id.profile -> {
                    itemSelected = ProfileFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewMainActivity, itemSelected).commit()
            true
        }
    }
}