package com.example.owlx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private var btnLogin : Button? = null
    private var btnCreateAccount : Button? = null
    private var btnForgotPassword : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btn_login)
        btnCreateAccount = findViewById(R.id.btn_criar_conta)
        btnForgotPassword = findViewById(R.id.btn_esquecer_senha)

        //TODO: Add user validation.

        btnLogin?.setOnClickListener {
            Toast.makeText(
                this,
                "Logging In!", Toast.LENGTH_LONG
            )
                .show()
        }

        btnCreateAccount?.setOnClickListener {
            Toast.makeText(
                this,
                "Let's create a new account?", Toast.LENGTH_LONG
            )
                .show()
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