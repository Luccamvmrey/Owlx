package com.example.owlx.models.product

data class Response(
    var products: List<Product>? = null,
    var exception: Exception? = null,
)
