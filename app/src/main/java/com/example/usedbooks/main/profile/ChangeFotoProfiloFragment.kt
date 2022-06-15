package com.example.usedbooks.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.usedbooks.R

class ChangeFotoProfiloFragment : Fragment() {
    val args = ChangeFotoProfiloFragmentArgs
    private lateinit var string_photo : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_foto_profilo, container, false)
        //string_photo = args.photo
        return view
    }
}