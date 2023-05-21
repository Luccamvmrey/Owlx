package com.example.owlx.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.owlx.R
import com.example.owlx.firebaseUtil.deleteDatabaseProduct
import com.example.owlx.firebaseUtil.updateDatabaseProduct
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyProductActivity : AppCompatActivity() {

    //Input variables
    private var nameInput: EditText? = null
    private var priceInput: EditText? = null
    private var descriptionInput: EditText? = null
    private var updateProductBtn: Button? = null
    private var deleteProductBtn: Button? = null

    private var ivSelectImage: ImageView? = null

    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_product)

        supportActionBar?.hide()

        backBtn = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }

        //Populates data
        val bundle = intent.extras

        val productName: String? = bundle!!.getString("productName")
        val productPrice: String? = bundle.getString("productPrice")
        val productDescription: String? = bundle.getString("productDescription")
        val productImage: String? = bundle.getString("productImage")
//        val userId: String? = bundle.getString("userId")

        populateData(productName, productPrice,
            productDescription, productImage)

        //Updates product
        updateProductBtn = findViewById(R.id.update_product_btn)
        updateProductBtn?.setOnClickListener {
            updateProduct(productName)
        }

        //Delete product
        deleteProductBtn = findViewById(R.id.delete_product_btn)
        deleteProductBtn?.setOnClickListener {
            deleteProduct(productName)
        }
    }

    //Updates product
    private fun updateProduct(productName: String?) {
        //Initializes firestore instance
        val db = Firebase.firestore

        //Required fields
        nameInput = findViewById(R.id.input_name_product)
        priceInput = findViewById(R.id.input_price_product)
        descriptionInput = findViewById(R.id.input_description_product)

        val name: String = nameInput?.text.toString()
        val price: Double = priceInput?.text.toString().toDouble()
        val description: String = descriptionInput?.text.toString()

        //Checks if any of the fields are empty
        if (name.isEmpty() || price.isNaN() || price == 0.0 || description.isEmpty()) {
            Toast.makeText(
                this,
                "Um ou mais campos ficaram vazios!", Toast.LENGTH_LONG
            )
                .show()

            return
        }

        updateDatabaseProduct(db, productName!!, name, price, description) {
            Toast.makeText(this,
            "Produto atualizado com sucesso!\nRedirecionando para página de produtos", Toast.LENGTH_SHORT
            )
                .show()

            finish()
        }
    }

    private fun deleteProduct(productName: String?) {
        //Initializes firestore instance
        val db = Firebase.firestore

        deleteDatabaseProduct(db, productName!!) {
            Toast.makeText(this,
                "Produto apagado com sucesso!\nRedirecionando para página de produtos", Toast.LENGTH_SHORT
            )
                .show()

            finish()
        }
    }

    private fun populateData(prodName: String?, prodPrice: String?,
                             prodDescription: String?, prodImageUrl: String?) {
        nameInput = findViewById(R.id.input_name_product)
        priceInput = findViewById(R.id.input_price_product)
        descriptionInput = findViewById(R.id.input_description_product)
        ivSelectImage = findViewById(R.id.product_image)


        nameInput?.setText(prodName)
        priceInput?.setText(prodPrice)
        descriptionInput?.setText(prodDescription)
        Glide.with(this).load(prodImageUrl).into(ivSelectImage!!)
    }
}