package com.example.owlx.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.owlx.HomePageActivity
import com.example.owlx.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    //Input variables
    private var emailInput : EditText? = null
    private var passwordInput : EditText? = null

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
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        //Creates and initializes variables
        val btnLogin: Button = findViewById(R.id.btn_login)
        val btnCreateAccount: Button = findViewById(R.id.btn_criar_conta)
        val btnForgotPassword: TextView = findViewById(R.id.btn_esquecer_senha)

        //User authentication
        //Initializes Firebase instance
        auth = Firebase.auth

        //Login Button logic
        btnLogin.setOnClickListener {
            loginUser()
        }

        //Create account Button logic
        btnCreateAccount.setOnClickListener {
            val intent = Intent(this,  SigninActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Forgot password Button/TextView logic
        btnForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        //User variables for authentication
        emailInput = findViewById(R.id.input_login)
        passwordInput = findViewById(R.id.input_password)

        val email: String = emailInput?.text.toString().trim { it <= ' '}
        val password: String = passwordInput?.text.toString().trim { it <= ' '}

        //Checks if e-mail or password fields are empty
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

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Redirects user to home page
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, displays a message to the user.
                    Toast.makeText(this,
                        "Falha no login, cheque seu e-mail e/ou sua senha.", Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
    }
}