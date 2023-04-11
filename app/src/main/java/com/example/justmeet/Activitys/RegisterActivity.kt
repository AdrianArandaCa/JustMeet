package com.example.justmeet.Activitys

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityRegisterBinding
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        binding.etBirthday.setOnClickListener {
            showDatePicker()
        }
        binding.btnRegister.setOnClickListener {
            finish()
        }
    }

    fun showDatePicker() {
        // Se crea un Calendar con la fecha actual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Se crea el DatePickerDialog
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            // Se establece la fecha seleccionada en el EditText
            val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            binding.etBirthday.setText(selectedDate)
        }, year, month, day)

        // Se muestra el DatePickerDialog
        datePickerDialog.show()
    }
}