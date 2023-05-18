package com.example.owlx.product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.owlx.R
import com.example.owlx.adapters.ItemAdapterProductsPage
import com.example.owlx.models.product.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyProductsActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = Firebase.auth
    private var user: FirebaseUser = auth.currentUser!!

    private val db = Firebase.firestore

    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products)
        
        supportActionBar?.hide()

        backBtn = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }
        
        //Shows products
        getMyProductsList { prodList ->
            //Set the LayoutManager that this RecyclerView will use.
            val recyclerViewItems: RecyclerView = findViewById(R.id.recycler_view_items_my_products)
            recyclerViewItems.layoutManager = LinearLayoutManager(this)

            //Initializes Adapter class, passes prodList as parameter
            val itemAdapterMyProductsPage = ItemAdapterProductsPage(this, prodList) { product ->

                val bundle = Bundle()
                bundle.putString("userId", product.userId)
                bundle.putString("productName", product.name)
                bundle.putString("productPrice", product.price.toString())
                bundle.putString("productDescription", product.description)
                bundle.putString("productImage", product.imageUri)

                val intent = Intent(this, MyProductActivity::class.java)
                intent.putExtras(bundle)

                startActivity(intent)
            }

            //Inflates items
            recyclerViewItems.adapter = itemAdapterMyProductsPage
        }
    }
    
    private fun getMyProductsList(callback: (prodList: ArrayList<Product>) -> Unit) {
        val myProductList = ArrayList<Product>()
        
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
                                if (doc.data["userId"].toString() == userID) {
                                    myProductList.add(doc.toObject(Product::class.java))
                                }
                            }

                            callback(myProductList)
                        }
                    }
            }
    }
}