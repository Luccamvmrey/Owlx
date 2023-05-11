package com.example.owlx.models.user

data class User(
    var name: String? = null,
    var email: String? = null,
    var coordinates: Coordinates? = null,
    var rating: Double? = null,
)
