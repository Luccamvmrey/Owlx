package com.example.owlx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import java.util.Calendar

class SigninActivity : AppCompatActivity() {

    private var btnDateOfBirth: EditText? = null
    private var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        btnDateOfBirth = findViewById(R.id.input_signin_date_of_birth)

        btnDateOfBirth?.setOnClickListener {

        }
    }

    private fun clickDatePicker() {

    }
}