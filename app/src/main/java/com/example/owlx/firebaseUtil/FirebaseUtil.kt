package com.example.owlx.firebaseUtil

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.owlx.models.product.Product
import com.example.owlx.models.user.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

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

fun getUserObjectFromUserId(db: FirebaseFirestore, userId: String, callback: (user: User) -> Unit) {
    db.collection("users").document(userId)
        .get()
        .addOnSuccessListener { doc ->
            val user = doc.toObject(User::class.java)

            callback(user!!)
        }
}

@RequiresApi(Build.VERSION_CODES.P)
fun getUriFromUploadedImage(imageUri: Uri, storage: FirebaseStorage, contentResolver: ContentResolver, callback: (downloadUri: Uri) -> Unit) {
    val storageRef = storage.reference

    val timestamp = System.currentTimeMillis().toString()
    val productImageRef = storageRef.child("images/$timestamp")
    @Suppress("DEPRECATION") val bitmap = when {
        //If found SDK if a version less than 28, use this method to get Bitmap
        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        else -> {
            //Otherwise use this
            val source = ImageDecoder.createSource(contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
    val baos = ByteArrayOutputStream()

    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)

    val fileInBytes = baos.toByteArray()
    val uploadTask = productImageRef.putBytes(fileInBytes)

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
@RequiresApi(Build.VERSION_CODES.P)
fun addProductToDatabase(db: FirebaseFirestore, storage: FirebaseStorage, contentResolver: ContentResolver, name: String, price: Double,
                         description: String, imageUri: Uri, callback: () -> Unit) {
    getUriFromUploadedImage(imageUri, storage, contentResolver) { downloadUri ->
        getUserFromLoggedUser(db) { _, userId ->
            db.collection("products")
                .add(Product(name, price, description, userId, downloadUri.toString()))
                .addOnSuccessListener {
                    callback()
                }
        }
    }
}

fun getProductId(db: FirebaseFirestore, prodName: String, callback: (prodId: String) -> Unit) {
    getUserFromLoggedUser(db) { _, userId ->
        db.collection("products")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { sp ->
                sp.forEach { doc ->
                    if (doc.data["name"] == prodName) {
                        val prodId = doc.id

                        callback(prodId)
                    }
                }
            }
    }
}

fun updateDatabaseProduct(db: FirebaseFirestore, oldName: String, newName: String, newPrice: Double,
                          newDescription: String, callback: () -> Unit) {
    getProductId(db, oldName) { prodId ->
        db.collection("products").document(prodId)
            .update(
                "name", newName,
                "price", newPrice,
                "description", newDescription
            )
            .addOnSuccessListener {
                callback()
            }
    }
}

