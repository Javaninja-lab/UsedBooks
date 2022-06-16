package com.example.usedbooks.main.profile

import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.usedbooks.R
import com.example.usedbooks.adapters.MaterialeRecyclerAdapter
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Materiale
import com.example.usedbooks.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val iv_foto_profilo = view.findViewById<ImageView>(R.id.iv_foto_profilo)
        //TODO(Cambiamento immagine profilo)
        iv_foto_profilo.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToChangeFotoProfiloFragment("null"/*Database.getLoggedStudent().*/)
            view.findNavController().navigate(action)
        }

        val tv_nome_cliente = view.findViewById<TextView>(R.id.tv_nome_cliente)
        tv_nome_cliente.text = "${getString(R.string.hi)}, ${Database.getLoggedStudent().nome}"

        val recyclerView  = view.findViewById<RecyclerView>(R.id.lv_ad_invendita)
        val tv_no_material = view.findViewById<TextView>(R.id.tv_no_material)
        val adapter = MaterialeRecyclerAdapter("profile", true)
        recyclerView.adapter = adapter
        val array = ArrayList<Materiale>()
        adapter.submitList(array)

        val pb_caricamento = view.findViewById<ProgressBar>(R.id.pb_caricamento)
        val progressDrawable: Drawable = pb_caricamento.indeterminateDrawable.mutate()
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        val color = typedValue.data
        progressDrawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        pb_caricamento.progressDrawable = progressDrawable

        thread(start = true) {
            for (m in Database.getMaterialiStudente(Database.getLoggedStudent().id,true)) {
                if (m != null)
                    array.add(m)
            }
            pb_caricamento.post {
                pb_caricamento.visibility = View.GONE
                if (array.isEmpty()) {
                    tv_no_material.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                }
            }
        }

        val recyclerView2  = view.findViewById<RecyclerView>(R.id.lv_ad_venduti)
        val tv_no_material2 = view.findViewById<LinearLayout>(R.id.ll_ad_vendit)
        val adapter2 = MaterialeRecyclerAdapter("profile", false)
        recyclerView2.adapter = adapter2
        val array2 = ArrayList<Materiale>()
        adapter2.submitList(array2)

        val pb_caricamento2 = view.findViewById<ProgressBar>(R.id.pb_caricamento)
        pb_caricamento2.progressDrawable = progressDrawable

        thread(start = true) {
            for (m in Database.getTransaction()) {
                if (m != null)
                    array2.add(m)
            }
            pb_caricamento.post {
                pb_caricamento2.visibility = View.GONE
                if (array2.isEmpty()) {
                    recyclerView2.visibility = View.GONE
                    tv_no_material2.visibility = View.VISIBLE
                } else {
                    recyclerView2.visibility = View.VISIBLE
                    adapter2.notifyDataSetChanged()
                }
            }
        }


        val ib_logout = view.findViewById<ImageButton>(R.id.ib_logout)
        ib_logout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        return view
    }

}