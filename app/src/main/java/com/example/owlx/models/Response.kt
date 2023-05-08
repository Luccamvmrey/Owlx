package com.example.owlx.models

data class Response(
    var products: List<Product>? = null,
    var exception: Exception? = null,
)
