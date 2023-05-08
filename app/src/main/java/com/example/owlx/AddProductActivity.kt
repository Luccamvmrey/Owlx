package com.example.owlx

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.owlx.login.LoginActivity
import com.example.owlx.models.Product
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class AddProductActivity : AppCompatActivity() {

    //Input variables
    private var nameInput: EditText? = null
    private var priceInput: EditText? = null
    private var descriptionInput: EditText? = null
    private var addProductBtn: Button? = null

    //Variables image selection
    private lateinit var tvSelectImage: TextView
    private lateinit var ivSelectImage: ImageView

    //Firebase
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var user: FirebaseUser

    //Drawer
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        //Initializes variables
        //Add product input
        addProductBtn = findViewById(R.id.add_product_btn)

        //Image selection
        var imageUri: Uri? = null
        tvSelectImage = findViewById(R.id.tv_select_image)
        ivSelectImage = findViewById(R.id.product_image)

        //Add product logic
        addProductBtn?.setOnClickListener {
            addProduct(imageUri)
        }

        //Image selection logic
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            imageUri = uri
            ivSelectImage.setImageURI(uri)
        }

        tvSelectImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        //Product data upload


        //Drawer logic
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Adicionar Produto"

        navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                //This will all be replaced eventually
                R.id.nav_home -> getHomePage()
                R.id.nav_settings -> Toast.makeText(applicationContext, "Settings clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> getProfilePage()
                R.id.nav_logout -> logoutUser()
                R.id.nav_message -> Toast.makeText(applicationContext, "Messages clicked", Toast.LENGTH_SHORT).show()
            }

            true
        }

        user = auth.currentUser!!
    }

    //Add Product Function
    private fun addProduct(imageUri: Uri?) {
        //Initializes firestore and storage instances
        val db = Firebase.firestore
        val storage: FirebaseStorage = Firebase.storage

        //Required fields
        user = auth.currentUser!!
        nameInput = findViewById(R.id.input_name_product)
        priceInput = findViewById(R.id.input_price_product)
        descriptionInput = findViewById(R.id.input_description_product)

        val name: String = nameInput?.text.toString()
        val price: Double = priceInput?.text.toString().toDouble()
        val description: String = descriptionInput?.text.toString()

        //Checks if any of the fields are empty
        if (name.isEmpty() || price.isNaN() || description.isEmpty() || imageUri == null) {
            Toast.makeText(
                this,
                "Um ou mais campos ficaram vazios!", Toast.LENGTH_LONG
            )
                .show()

            return
        }

        //Image Uri upload
        val storageRef = storage.reference

        //Uploads image
        val productImageRef = storageRef.child("images/${imageUri.lastPathSegment}")
        val uploadTask = productImageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            Log.d(TAG, "Image uploaded successfully")
        }

        //Gets image url
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            productImageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                //Gets userID, stores product in Firestore
                val usersRef = db.collection("users")
                usersRef
                    .whereEqualTo("email", user.email.toString())
                    .get().addOnSuccessListener { sp ->
                        sp?.forEach { doc ->
                            if (doc != null) {
                                val userId = doc.id

                                db.collection("products")
                                    .add(Product(name, price, description, userId, downloadUri))
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "Produto criado com sucesso, redirecionando para página inicial", Toast.LENGTH_LONG
                                        )
                                            .show()

                                        val intent = Intent(this, HomePageActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                            }
                        }
                    }


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

    private fun getProfilePage() {
        val intent = Intent(this, UserPageActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getHomePage() {
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
        finish()
    }
}