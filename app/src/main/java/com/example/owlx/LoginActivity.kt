package com.example.owlx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private var btnLogin : Button? = null
    private var btnCreateAccount : Button? = null
    private var btnForgotPassword : TextView? = null
    private var emailInput : EditText? = null
    private var passwordInput : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btn_login)
        btnCreateAccount = findViewById(R.id.btn_criar_conta)
        btnForgotPassword = findViewById(R.id.btn_esquecer_senha)
        emailInput = findViewById(R.id.input_login)
        passwordInput = findViewById(R.id.input_password)

        //TODO: Add user validation.

        btnLogin?.setOnClickListener {
            var userEmail : String
            var userPassword : String

            //This is just a test, I won't keep it like that, don't worry!
            emailInput?.text?.let { it1 ->
                userEmail = it1.toString()

                passwordInput?.text?.let {
                    userPassword = it.toString()

                    Toast.makeText(
                        this,
                        "Logging user: $userEmail, password: $userPassword", Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }

        btnCreateAccount?.setOnClickListener {
            val intent = Intent(this,  SigninActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnForgotPassword?.setOnClickListener {
            Toast.makeText(
                this,
                "You forgot your password? How could you??", Toast.LENGTH_LONG
            )
                .show()
        }
    }
}