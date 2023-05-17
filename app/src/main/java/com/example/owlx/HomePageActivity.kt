package com.example.owlx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.owlx.adapters.ItemAdapter
import com.example.owlx.login.LoginActivity
import com.example.owlx.models.product.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = Firebase.auth
    private var user: FirebaseUser = auth.currentUser!!

    private val db = Firebase.firestore

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        //Floating Button
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener {
            getAddProductPage()
        }

        //Show products
        getProductList { prodList ->
            //Set the LayoutManager that this RecyclerView will use.
            val recyclerViewItems: RecyclerView = findViewById(R.id.recycler_view_items)
            recyclerViewItems.layoutManager = LinearLayoutManager(this)

            //Initializes Adapter class, passes prodList as parameter
            val itemAdapter = ItemAdapter(this, prodList) { product ->

                val bundle = Bundle()
                bundle.putString("userId", product.userId)
                bundle.putString("productName", product.name)
                bundle.putString("productPrice", product.price.toString())
                bundle.putString("productDescription", product.description)
                bundle.putString("productImage", product.imageUri)

                val intent = Intent(this, ProductActivity::class.java)
                intent.putExtras(bundle)

                startActivity(intent)
            }

            //Inflates items
            recyclerViewItems.adapter = itemAdapter
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
    }

    //Get products
    private fun getProductList(callback: (prodList: ArrayList<Product>) -> Unit) {
        val productList = ArrayList<Product>()

        val usersRef = db.collection("users")

        db.collection("products")
            .get()
            .addOnSuccessListener { result ->

                usersRef
                    .whereEqualTo("email", user.email.toString())
                    .get()
                    .addOnSuccessListener { sp ->
                        sp.forEach { user ->
                            val userID = user.id

                            //Filters products
                            for (doc in result) {
                                if (doc.data["userId"].toString() != userID) {
                                    productList.add(doc.toObject(Product::class.java))
                                }
                            }

                            callback(productList)
                        }
                    }
            }
    }

    //Add product page
    private fun getAddProductPage() {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
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
    }
}