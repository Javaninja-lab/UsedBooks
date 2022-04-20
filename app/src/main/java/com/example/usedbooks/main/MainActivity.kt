package com.example.usedbooks.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.usedbooks.NuovoAnnuncioActivity
import com.example.usedbooks.R
import com.example.usedbooks.main.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val homeFragment : Fragment = HomeFragment.newInstance()
    private val searchFragment : Fragment = SearchFragment.newInstance()
    private val chatFragment : Fragment = ChatFragment()
    private val profileFragment : Fragment = ProfileFragment()
    private var lastFinestra : TipoFinestra = TipoFinestra.Home


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerViewMainActivity, homeFragment).commit()

        bottomNavigationView.setOnItemSelectedListener {
            var itemSelected : Fragment = homeFragment
            var tipoFinestra : TipoFinestra = TipoFinestra.Home
            when (it.itemId) {
                R.id.home -> {
                    itemSelected = homeFragment
                    tipoFinestra = TipoFinestra.Home
                }
                R.id.search -> {
                    itemSelected = searchFragment
                    tipoFinestra = TipoFinestra.Search
                }
                R.id.chat -> {
                    itemSelected = chatFragment
                    tipoFinestra = TipoFinestra.Chat
                }
                R.id.profile -> {
                    itemSelected = profileFragment
                    tipoFinestra = TipoFinestra.Profile
                }
            }
            val previousFragment = supportFragmentManager.fragments.last()
            if(previousFragment != itemSelected) {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewMainActivity, itemSelected).addToBackStack(null).commit()
                lastFinestra = tipoFinestra
            }
            true
        }
        buttonAddAnnuncio.setOnClickListener {
            val i = Intent(this, NuovoAnnuncioActivity::class.java)
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    enum class TipoFinestra {
        Home,
        Search,
        Chat,
        Profile
    }
}