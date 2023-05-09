package com.example.owlx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.owlx.login.LoginActivity
import com.example.owlx.models.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserPageActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle

    private var tvGreetings: TextView? = null
    private var tvEmail: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Minha Conta"

        navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                //This will all be replaced eventually
                R.id.nav_home -> getHomePage()
                R.id.nav_settings -> Toast.makeText(applicationContext, "Settings clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(applicationContext, "Você já está nessa página", Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> logoutUser()
                R.id.nav_message -> Toast.makeText(applicationContext, "Messages clicked", Toast.LENGTH_SHORT).show()
            }

            true
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

    //Drawer Functions
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun logoutUser() {
        //Logs user out
        FirebaseAuth.getInstance().signOut()

        //Redirects to login page
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getHomePage() {
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
        finish()
    }
}