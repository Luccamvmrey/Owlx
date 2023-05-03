package com.example.owlx.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.owlx.HomePageActivity
import com.example.owlx.R
import com.example.owlx.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SigninActivity : AppCompatActivity() {

    //Input variables
    private var nameInput: EditText? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var postRegistrationLogin: TextView? = null
    private lateinit var btnCreateAccount: Button

    //Firebase
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        //Checks if user is logged in, if they are, redirects them to home page
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        supportActionBar?.hide()

        //User authentication
        //Initializes Firebase instance
        auth = Firebase.auth

        //Initializes variables
        btnCreateAccount = findViewById(R.id.btn_criar_conta)

        btnCreateAccount.setOnClickListener {
            createAccount()
        }
    }

    //Sign-In Functions
    private fun createAccount() {
        //Initializes Firestore instance
        val db = Firebase.firestore

        //Fields required for firebase authentication
        emailInput = findViewById(R.id.input_signin_email)
        passwordInput = findViewById(R.id.input_signin_password)

        //Extra fields
        nameInput = findViewById(R.id.input_signin_name)

        //Other variables
        postRegistrationLogin = findViewById(R.id.login_registro)

        val email: String = emailInput?.text.toString().trim { it <= ' '}
        val password: String = passwordInput?.text.toString().trim { it <= ' '}
        val name: String = nameInput?.text.toString().trim { it <= ' '}

        //Checks if e-mail, password or name fields are empty
        if (email.isEmpty()) {
            Toast.makeText(
                this,
                "Por favor, insira um e-mail!", Toast.LENGTH_LONG
            )
                .show()

            return
        }

        if (password.isEmpty()) {
            Toast.makeText(
                this,
                "Por favor, insira uma senha!", Toast.LENGTH_LONG
            )
                .show()

            return
        }

        if (name.isEmpty()) {
            Toast.makeText(
                this,
                "Por favor, insira seu nome!", Toast.LENGTH_LONG
            )
                .show()

            return
        }

        //Adds user to database
        val user = User(name, email)
        db.collection("users")
            .add(user.toHashMap())

        //Creates user using default firebase method
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, displays a message to the user
                    Toast.makeText(
                        this,
                        "Cadastro concluído com sucesso.", Toast.LENGTH_SHORT
                    )
                        .show()

                    //Makes TextView visible after registration was completed
                    postRegistrationLogin?.visibility = View.VISIBLE

                    //Takes user back to login page after click
                    postRegistrationLogin?.setOnClickListener {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    // If sign in fails, displays a message to the user.
                    Toast.makeText(
                        this,
                        "Falha no cadastro.", Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
    }
}