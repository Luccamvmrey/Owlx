package com.example.owlx.models.product

import android.net.Uri

data class Product(
    var name: String? = null,
    var price: Double? = null,
    var description: String? = null,
    var userId: String? = null,
    var imageUri: String? = null,
)
