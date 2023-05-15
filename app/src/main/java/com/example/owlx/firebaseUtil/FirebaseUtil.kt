package com.example.owlx.firebaseUtil

import android.net.Uri
import com.example.owlx.models.product.Product
import com.example.owlx.models.user.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

private val userAuth = Firebase.auth.currentUser

fun getUserFromLoggedUser(db: FirebaseFirestore, callback: (user: User, userId: String) -> Unit) {
    val usersRef = db.collection("users")

    usersRef
        .whereEqualTo("email", userAuth?.email.toString())
        .get()
        .addOnSuccessListener { sp ->
            sp?.forEach { doc ->
                val userId = doc.id
                val user = doc.toObject(User::class.java)

                callback(user, userId)
            }
        }
}

fun getUserRefFromUserId(db: FirebaseFirestore, callback: (user: User, userRef: DocumentReference) -> Unit) {
    getUserFromLoggedUser(db) { user, userId ->
        val userRef = db.collection("users").document(userId)

        callback(user, userRef)
    }
}

fun getUriFromUploadedImage(imageUri: Uri, storage: FirebaseStorage, callback: (downloadUri: Uri) -> Unit) {
    val storageRef = storage.reference
    val productImageRef = storageRef.child("images/${imageUri.lastPathSegment}")
    val uploadTask = productImageRef.putFile(imageUri)

    uploadTask
        .continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            productImageRef.downloadUrl
        }
        .addOnCompleteListener { task ->
            val downloadUri = task.result

            callback(downloadUri)
        }
}

//Slightly better callback hell, you're welcome
fun addProductToDatabase(db: FirebaseFirestore, storage: FirebaseStorage, name: String, price: Double,
                         description: String, imageUri: Uri, callback: () -> Unit) {
    getUriFromUploadedImage(imageUri, storage) { downloadUri ->
        getUserFromLoggedUser(db) { _, userId ->
            db.collection("products")
                .add(Product(name, price, description, userId, downloadUri.toString()))
                .addOnSuccessListener {
                    callback()
                }
        }
    }
}