package com.example.owlx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {

    private lateinit var logoutBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        auth = Firebase.auth
        user = auth.currentUser!!

        logoutBtn = findViewById(R.id.btn_logout)

        logoutBtn.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        //Logs user out
        FirebaseAuth.getInstance().signOut()

        //Redirects to login page
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}