package com.example.usedbooks.dataClass

import android.graphics.Bitmap
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.usedbooks.R
import com.example.usedbooks.adapters.UserAdapter
import com.example.usedbooks.customView.PersonalProgressBar
import com.example.usedbooks.main.chat.ListChatsFragmentDirections
import com.example.usedbooks.main.profile.ProfileFragmentDirections
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest
import kotlin.concurrent.thread

abstract class Gestore {

    companion object {
        val md = MessageDigest.getInstance("SHA-256")

        fun ByteArray.toHex() = joinToString(separator = "") {
            "%02x".format(it)
        }

        fun getHash(input : String) : String {
            return md.digest(input.toByteArray(UTF_8)).toHex()
        }

        fun setBitmap(materiale : Materiale, imageView: ImageView, conThread: Boolean = true) {
            if(conThread){
                thread(start = true) {
                    val photoBitmap : Bitmap = Database.getPhotoMateriale(materiale.photos[0])
                    imageView.post {
                        imageView.setImageBitmap(photoBitmap)
                    }
                }
            } else {
                val photoBitmap : Bitmap = Database.getPhotoMateriale(materiale.photos[0])
                imageView.post {
                    imageView.setImageBitmap(photoBitmap)
                }
            }
        }

        fun getProfileImage(iv_foto_profilo: ImageView, userid: String) {
            val uriImageStudent = Database.getUriPhotosStudente(userid)
            if (!uriImageStudent.equals(""))
                iv_foto_profilo.setImageBitmap(Database.getPhotoStudente(uriImageStudent))
            else {
                iv_foto_profilo.setImageResource(R.drawable.placeholder)
            }
        }

        fun bindUserAdapter(
            position: Int,
            holder: UserAdapter.UserViewHolder,
            userList : ArrayList<User>,
            runnable : Runnable
        ) {
            val currentUser = userList[position]
            holder.tv_nome_studente.text = currentUser.username

            if (currentUser.id != null) {
                getProfileImage(holder.iv_foto_profilo, currentUser.id)
            } else {
                holder.iv_foto_profilo.setImageResource(R.drawable.placeholder)
            }
            holder.iv_foto_profilo.clipToOutline = true

            val messaggio = Database.getLastMessage(currentUser)
            if (messaggio?.message != null) {
                val MAX_LETTER = 70
                var ultimoMessaggio = messaggio.message
                if(ultimoMessaggio.length > MAX_LETTER) {
                    ultimoMessaggio = ultimoMessaggio.removeRange(MAX_LETTER until ultimoMessaggio.length)
                    ultimoMessaggio = ultimoMessaggio.plus("...")
                }
                holder.tv_ultimo_messaggio.text =
                    "${holder.tv_ultimo_messaggio.text}: $ultimoMessaggio"
            } else {
                holder.tv_ultimo_messaggio.text = holder.itemView.context.getString(R.string.no_message)
            }

            holder.itemView.setOnClickListener{
                runnable.run()
            }
        }

        fun bindMaterialViewHolder(materiale: Materiale, itemView : View, button : Boolean) {
            val iv_foto_materiale = itemView.findViewById<ImageView>(R.id.iv_foto_materiale)
            val pb_image = itemView.findViewWithTag<PersonalProgressBar>("pb_image")
            pb_image.caricamento {
                setBitmap(materiale, iv_foto_materiale, false)
                itemView.post {
                    pb_image.visibility = View.GONE
                    iv_foto_materiale.visibility = View.VISIBLE
                }
            }

            val tv_nome_materiale = itemView.findViewById<TextView>(R.id.tv_nome_materiale)
            tv_nome_materiale.text = materiale.nome
            val tv_prezzo = itemView.findViewById<TextView>(R.id.tv_prezzo)

            if(button) {
                tv_prezzo.text = "${tv_prezzo.text}: ${materiale.prezzo}$"
                val btn_sold = itemView.findViewById<Button>(R.id.btn_sold)
                btn_sold.setOnClickListener {
                    val action = ProfileFragmentDirections.actionProfileFragmentToSoldFragment(materiale)
                    it.findNavController().navigate(action)
                }
            } else {
                tv_prezzo.text = "${materiale.prezzo}$"
                val tv_nome_venditore = itemView.findViewById<TextView>(R.id.tv_nome_venditore)
                tv_nome_venditore.text = "${tv_nome_venditore.text}: ${Database.getStudenteFromId(materiale.proprietario)?.nome}"
                val tv_state_material = itemView.findViewById<TextView>(R.id.tv_state_material)
                tv_state_material.text = "${tv_state_material.text}: ${materiale.stato}"
            }
        }

    }
}