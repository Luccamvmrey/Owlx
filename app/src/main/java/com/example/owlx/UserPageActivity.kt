package com.example.owlx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.owlx.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)



    }

    private fun populateData() {
        //Initializes Firestore instance
        val db = Firebase.firestore

        //TODO: FIX THIS

        //Gets current user, gets user from users collection using email
        val userAuth = Firebase.auth.currentUser
        val usersRef = db.collection("users")
//        usersRef
//            .whereEqualTo("email", userAuth?.email.toString())
//            .get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    val user = User(document.toHashSet())
//                }
//            }


    }
}