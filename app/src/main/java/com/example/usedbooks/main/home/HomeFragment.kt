package com.example.usedbooks.main.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.usedbooks.R
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val adapter = MaterialeAdapter(this.requireContext(), Database.getMateriali())
        val lv_home = view.findViewById<ListView>(R.id.lv_home)
        lv_home.adapter = adapter
        lv_home.setOnItemClickListener { adapterView, view, position, id ->
            val action = HomeFragmentDirections.actionHomeFragmentToMaterialeFragment(adapterView.getItemAtPosition(position) as Materiale)
            view.findNavController().navigate(action)
        }
        return view
    }
}

class MaterialeAdapter(private val context: Context,
       private val dataSource: ArrayList<Materiale>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Materiale? {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        var rowView : View
        if(convertView == null)
            rowView = inflater.inflate(R.layout.list_item_home, parent, false)
        else
            rowView = convertView

        val materiale = getItem(position)

        val iv_foto_materiale = rowView.findViewById<ImageView>(R.id.iv_foto_materiale)
        //TODO(Mettere immagine presa dalla classe)
        val tv_nome_materiale = rowView.findViewById<TextView>(R.id.tv_nome_materiale)
        tv_nome_materiale.setText(materiale?.nome)
        val tv_nome_venditore = rowView.findViewById<TextView>(R.id.tv_nome_venditore)
        tv_nome_venditore.setText(materiale?.proprietario)
        val tv_prezzo = rowView.findViewById<TextView>(R.id.tv_prezzo)
        tv_prezzo.setText(materiale?.prezzo.toString())
        return rowView
    }

}
