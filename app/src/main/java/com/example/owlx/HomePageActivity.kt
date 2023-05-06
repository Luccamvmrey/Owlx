package com.example.owlx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.owlx.login.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        //Floating Button
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener {
            getAddProductPage()
        }

        //Drawer logic
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Home"

        navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                //This will all be replaced eventually
                R.id.nav_home -> Toast.makeText(applicationContext, "Home clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_settings -> Toast.makeText(applicationContext, "Settings clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> getProfilePage()
                R.id.nav_logout -> logoutUser()
                R.id.nav_message -> Toast.makeText(applicationContext, "Messages clicked", Toast.LENGTH_SHORT).show()
            }

            true
        }

        auth = Firebase.auth
        user = auth.currentUser!!
    }

    private fun getAddProductPage() {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
        finish()
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

    private fun getProfilePage() {
        val intent = Intent(this, UserPageActivity::class.java)
        startActivity(intent)
        finish()
    }
}