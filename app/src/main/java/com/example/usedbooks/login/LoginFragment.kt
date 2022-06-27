package com.example.usedbooks.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.example.usedbooks.R
import com.example.usedbooks.customView.PersonalProgressBar
import com.example.usedbooks.dataClass.Database
import com.example.usedbooks.dataClass.Gestore
import com.example.usedbooks.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    private lateinit var cl_login: ConstraintLayout
    private lateinit var pb_login: PersonalProgressBar
    private lateinit var btn_SingIn: Button
    private lateinit var btn_Login: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_login, container, false)

        auth = FirebaseAuth.getInstance()

        btn_SingIn = layout.findViewById(R.id.btn_SingIn)
        btn_SingIn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        cl_login = layout.findViewById(R.id.cl_login)

        pb_login = PersonalProgressBar(layout.context, cl_login, 250)
        pb_login.visibility = View.GONE

        btn_Login = layout.findViewById(R.id.btn_Login)
        btn_Login.setOnClickListener {
            btn_Login.visibility = View.GONE
            btn_SingIn.visibility = View.GONE
            btn_Login.isEnabled = false
            btn_SingIn.isEnabled = false
            pb_login.visibility = View.VISIBLE
            pb_login.caricamento {
                onLoginClick(layout)
            }
        }
        return layout
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null && view != null) {
            login(currentUser.email!!, requireView())
        }
    }

    private fun onLoginClick(view : View) {
        val emailEditText = view.findViewById<EditText>(R.id.et_email_login)
        val passwordEditText = view.findViewById<EditText>(R.id.et_password_login)
        val email = emailEditText.text.toString().trim()
        val passwordNotHashed = passwordEditText.text.toString().trim()
        var error = false
        if (email.isEmpty()) {
            error = true
            val msg = "Enter email"
            view.post {
            emailEditText.error = msg
                Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
                btn_Login.isEnabled = true
                btn_SingIn.isEnabled = true
                btn_Login.visibility = View.VISIBLE
                btn_SingIn.visibility = View.VISIBLE
                pb_login.visibility = View.GONE
            }
        }
        if (passwordNotHashed.isEmpty()) {
            error = true
            val msg = "Enter password"
            view.post {
            passwordEditText.error = msg
                Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
                btn_Login.isEnabled = true
                btn_SingIn.isEnabled = true
                btn_Login.visibility = View.VISIBLE
                btn_SingIn.visibility = View.VISIBLE
                pb_login.visibility = View.GONE
            }
        }
        if(!error) {
            val password = Gestore.getHash(passwordNotHashed)
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    login(email, view)
                } else {
                    view.post {
                        Toast.makeText(view.context, "Credenziali errate", Toast.LENGTH_LONG).show()
                    }
                }
                btn_Login.isEnabled = true
                btn_SingIn.isEnabled = true
                btn_Login.visibility = View.VISIBLE
                btn_SingIn.visibility = View.VISIBLE
                pb_login.visibility = View.GONE
            }
        }
    }

    private fun login(email : String, view : View) {
        btn_Login.isEnabled = false
        btn_SingIn.isEnabled = false
        btn_Login.visibility = View.GONE
        btn_SingIn.visibility = View.GONE
        pb_login.visibility = View.VISIBLE
        pb_login.caricamento {
            val studente = Database.getStudente(email)
            if(studente!=null) {
                Database.setLoggedStudent(studente)
                val intent = Intent(view.context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                pb_login.post {
                    btn_Login.isEnabled = true
                    btn_SingIn.isEnabled = true
                    btn_Login.visibility = View.VISIBLE
                    btn_SingIn.visibility = View.VISIBLE
                    pb_login.visibility = View.GONE
                }
            }
        }

    }
}