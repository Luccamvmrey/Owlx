package com.example.owlx

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.owlx.firebaseUtil.addProductToDatabase
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

    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        supportActionBar?.hide()

        backBtn = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }

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
    }

    //Add Product Function
    private fun addProduct(imageUri: Uri?) {
        //Initializes firestore and storage instances
        val db = Firebase.firestore
        val storage: FirebaseStorage = Firebase.storage

        //Required fields
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

        addProductToDatabase(db, storage, name, price, description, imageUri) {
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