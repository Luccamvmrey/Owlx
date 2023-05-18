package com.example.owlx.user

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
import com.example.owlx.R
import com.example.owlx.firebaseUtil.getUserFromLoggedUser
import com.example.owlx.firebaseUtil.getUserRefFromUserId
import com.example.owlx.models.user.Coordinates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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

        //Populates data and modifies TextView based on user coordinates
        //Initializes views
        tvGreetings = findViewById(R.id.tv_greetings)
        tvEmail = findViewById(R.id.tv_email)
        tvLocalWarning = findViewById(R.id.location_warning)
        addLocationBtn = findViewById(R.id.location_btn)

        //Gets current user, gets user from users collection using email
        getUserFromLoggedUser(db) { user, _ ->
            val greetingText = getString(R.string.welcome, user.name)

            tvGreetings.text = greetingText
            tvEmail.text = user.email

            if (user.coordinates != null) {
                val coordinatesText = getString(
                    R.string.localization,
                    user.coordinates!!.longitude.toString(),
                    user.coordinates!!.latitude.toString())

                tvLocalWarning.text = coordinatesText
                addLocationBtn.visibility = View.GONE
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

                    getUserRefFromUserId(db) { user ,userRef ->
                        userRef
                            .update("coordinates", coordinates)
                            .addOnSuccessListener {
                                if (user.coordinates != null) {

                                    //Refreshes activity
                                    finish()
                                    overridePendingTransition(0,0)
                                    startActivity(intent)
                                    overridePendingTransition(0,0)
                                }

                                Toast.makeText(this,
                                    "Localização obtida com sucesso!", Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this,
                    "Falha na localização", Toast.LENGTH_SHORT
                    )
                        .show()
                }
        }
    }
}