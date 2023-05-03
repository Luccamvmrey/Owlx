package com.example.owlx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private var emailInput: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val btnForgotPassword: Button = findViewById(R.id.btn_recuperar_senha)

        btnForgotPassword.setOnClickListener {
            forgotPassword()
        }
    }

    private fun forgotPassword() {
        //Variable for password recovery
        emailInput = findViewById(R.id.input_forgot_password_email)

        val email: String = emailInput?.text.toString().trim { it <= ' '}

        //Checks if e-mail field is empty
        if (email.isEmpty()) {
            Toast.makeText(
                this,
                "Por favor, insira um e-mail!", Toast.LENGTH_LONG
            )
                .show()

            return
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "E-mail enviado!", Toast.LENGTH_LONG
                    )
                        .show()

                    finish()
                } else {
                    Toast.makeText(
                        this,
                        task.exception!!.message.toString(), Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
    }
}