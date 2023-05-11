package com.example.owlx

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.owlx.models.user.Coordinates
import com.example.owlx.models.user.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserPageActivity : AppCompatActivity() {

    private lateinit var backBtn: Button

    private lateinit var tvGreetings: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvLocalWarning: TextView
    private lateinit var addLocationBtn: Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionRequestCode = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)

        supportActionBar?.hide()

        //Initializes Firestore instance
        val db = Firebase.firestore

        //Back button Logic
        backBtn = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }

        var userId: String? = null

        //Populates data and modifies TextView based on user coordinates
        //Initializes views
        tvGreetings = findViewById(R.id.tv_greetings)
        tvEmail = findViewById(R.id.tv_email)
        tvLocalWarning = findViewById(R.id.location_warning)
        addLocationBtn = findViewById(R.id.location_btn)

        //Gets current user, gets user from users collection using email
        val userAuth = Firebase.auth.currentUser
        val usersRef = db.collection("users")
        var user: User? = null


        usersRef
            .whereEqualTo("email", userAuth?.email.toString())
            .get()
            .addOnSuccessListener { sp ->
                sp?.forEach { doc ->
                    userId = doc.id
                    user = doc.toObject(User::class.java)
                    val greetingText = getString(R.string.welcome, user!!.name)

                    tvGreetings.text = greetingText
                    tvEmail.text = user!!.email

                    if (user!!.coordinates != null) {
                        val coordinatesText = getString(R.string.localization,
                            user!!.coordinates!!.longitude.toString(),
                            user!!.coordinates!!.latitude.toString())


                        tvLocalWarning.text = coordinatesText
                        addLocationBtn.visibility = View.GONE
                    }
                }
            }

        //Location Logic
        addLocationBtn.setOnClickListener {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                //Request permission
                ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionRequestCode
                )

                return@setOnClickListener
            }

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    val coordinates = Coordinates(location.longitude, location.latitude)

                    Log.d(TAG, "Coordinates: $coordinates")

                    val userRef = db.collection("users").document(userId!!)
                    userRef
                        .update("coordinates", coordinates)
                        .addOnSuccessListener {
                            if (user!!.coordinates != null) {
                                val coordinatesText = getString(R.string.localization,
                                    user!!.coordinates!!.longitude.toString(),
                                    user!!.coordinates!!.latitude.toString())


                                tvLocalWarning.text = coordinatesText
                                addLocationBtn.visibility = View.GONE
                            }
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this,
                    "Falha na localização", Toast.LENGTH_SHORT
                    )
                        .show()
                }

            Log.d(TAG, "This is the UserID: $userId")
        }
    }
}