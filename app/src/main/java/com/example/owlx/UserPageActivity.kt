package com.example.owlx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.owlx.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserPageActivity : AppCompatActivity() {

    private lateinit var backBtn: Button

    private var tvGreetings: TextView? = null
    private var tvEmail: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)

        supportActionBar?.hide()

        backBtn = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }

        populateData()
    }

    private fun populateData() {
        //Initializes Firestore instance
        val db = Firebase.firestore

        //Gets TextView
        tvGreetings = findViewById(R.id.tv_greetings)
        tvEmail = findViewById(R.id.tv_email)

        //Gets current user, gets user from users collection using email
        val userAuth = Firebase.auth.currentUser
        val usersRef = db.collection("users")

        usersRef
            .whereEqualTo("email", userAuth?.email.toString())
            .get()
            .addOnSuccessListener { sp ->
                sp?.forEach { doc ->
                    val user = doc.toObject(User::class.java)
                    val greetingText = getString(R.string.welcome, user.name)

                    tvGreetings?.text = greetingText
                    tvEmail?.text = user.email
                }
            }
    }
}