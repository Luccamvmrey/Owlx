package com.example.owlx.product

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.owlx.R

class ProductActivity : AppCompatActivity() {

    private var tvProductName: TextView? = null
    private var tvProductPrice: TextView? = null
    private var ivProductImage: ImageView? = null
    private var tvProductDescription: TextView? = null

    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        supportActionBar?.hide()

        backBtn = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }

        val bundle = intent.extras

        val productName: String? = bundle!!.getString("productName")
        val productPrice: String? = bundle.getString("productPrice")
        val productDescription: String? = bundle.getString("productDescription")
        val productImage: String? = bundle.getString("productImage")
        val sellerId: String? = bundle.getString("userId")

        Log.d(TAG, "Seller id: $sellerId")

        populateData(productName, productPrice,
            productDescription, productImage)
    }

    private fun populateData(prodName: String?, prodPrice: String?,
                             prodDescription: String?, prodImageUrl: String?) {
        tvProductName = findViewById(R.id.tv_product_name)
        tvProductPrice = findViewById(R.id.tv_product_price)
        tvProductDescription = findViewById(R.id.tv_product_description)
        ivProductImage = findViewById(R.id.product_image)

        tvProductName?.text = prodName
        tvProductPrice?.text = String.format("R$ %.2f", prodPrice?.toFloat())
        tvProductDescription?.text = prodDescription
        Glide.with(this).load(prodImageUrl).into(ivProductImage!!)
    }
}