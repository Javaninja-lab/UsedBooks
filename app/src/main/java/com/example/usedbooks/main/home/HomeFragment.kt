package com.example.usedbooks.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.adapters.MaterialeRecyclerAdapter
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView  = view.findViewById<RecyclerView>(R.id.lv_home)
        val adapter = MaterialeRecyclerAdapter()
        recyclerView.adapter = adapter
        //val executor= Executors.newSingleThreadExecutor()
        var response: ArrayList<Materiale> = ArrayList()
        adapter.submitList(response)
        Toast.makeText(requireContext(),"ciao", Toast.LENGTH_LONG).show()
       /*executor.execute {
            try {
                //response = Database.getMateriali()
                for ( materiale in Database.getMateriali()){
                    response.add(materiale)
                }
                adapter.notifyDataSetChanged()
                //resultHandler.post { callback(response) }
            } catch (e: Exception) {
                //val errorResult = Result.Error(e)
                //resultHandler.post { callback(errorResult) }
            }
        }*/

        thread(start = true) {
            response.clear()
            for ( materiale in Database.getMateriali()){
                response.add(materiale)
            }
            this.activity?.runOnUiThread{
                adapter.notifyDataSetChanged()
            }

        }





        return view
    }
}