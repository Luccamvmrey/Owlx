package com.example.owlx.models

class User (private var name: String, private val email: String) {

    constructor(map: HashMap<String, Any>) : this(
        name = map["name"] as String
        , email = map["email"] as String
    )

    //TODO: Add dateOfBirth, rating, and whatever more you see fit
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "email" to email,
        )
    }
}