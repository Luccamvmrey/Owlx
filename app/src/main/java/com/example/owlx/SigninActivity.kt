package com.example.owlx

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import java.util.*

class SigninActivity : AppCompatActivity() {

    private var btnDateOfBirth: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        btnDateOfBirth = findViewById(R.id.input_signin_date_of_birth)

        //Prevents keyboard from being displayed.
        btnDateOfBirth?.showSoftInputOnFocus = false

        btnDateOfBirth?.setOnClickListener {
            clickDatePicker()
        }

        //TODO: User validation, same shit as before, just do it
    }

    private fun clickDatePicker() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                val formattedDay: String = if (selectedDayOfMonth < 10) {
                    "0$selectedDayOfMonth"
                } else {
                    "$selectedDayOfMonth"
                }

                val formattedMonth: String = if (selectedMonth < 10) {
                    "0${selectedMonth + 1}"
                } else {
                    "${selectedMonth + 1}"
                }

                val selectedDate = "$formattedDay/$formattedMonth/$selectedYear"

                btnDateOfBirth?.setText(selectedDate)
            },
            year,
            month,
            day
        )

        dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dpd.show()
    }
}