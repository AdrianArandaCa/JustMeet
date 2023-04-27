package com.example.justmeet.Activitys

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.Location
import com.example.justmeet.Models.Setting
import com.example.justmeet.Models.User
import com.example.justmeet.databinding.ActivityRegisterBinding
import kotlinx.coroutines.*
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.CoroutineContext

class RegisterActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityRegisterBinding
    var job = Job()
    var userPosted: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        var isMale: Boolean = false
        var isFemale: Boolean = true
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
//        binding.etBirthday.setOnClickListener {
//            showDatePicker()
//        }
        binding.btnBackRegister.setOnClickListener {
            onBackPressed()
        }
        binding.rbMale.setOnClickListener {
            binding.rbFemale.isChecked = false
            isMale = true
            isFemale = false
        }
        binding.rbFemale.setOnClickListener {
            binding.rbMale.isChecked = false
            isFemale = true
            isMale = false
        }
        binding.btnRegister.setOnClickListener {
            if(binding.etBirthday.text.toString().toInt() < 18 || binding.etBirthday.text.toString().toInt() > 99 ) {
                Toast.makeText(this, "Edad no valida", Toast.LENGTH_LONG)
                    .show()
            }
            var nomUser = binding.etUserName.text.toString()
            var passWord = binding.etPassword.text.toString()
            var passWordConfirm = binding.etPasswordConfirm.text.toString()
            var email = binding.etEmail.text.toString()
            var dateOfBirthday = binding.etBirthday.text.toString().toInt()
            //val sqlDate = java.sql.Date(dateOfBirthday.time)


            var genre = ""
            if (isMale) {
                genre = "M"
            } else if (isFemale) {
                genre = "F"
            }
            if (nomUser.isNotEmpty() && passWord.isNotEmpty() && passWordConfirm.isNotEmpty() && email.isNotEmpty() && binding.etBirthday.text.isNotEmpty() && genre.isNotEmpty()) {
                if (passWord.equals(passWordConfirm)) {
                    //Guardas usuario
                    var passWordEncrypt = encryptPassword(passWord)
                    var user = User(
                        null,
                        nomUser,
                        passWordEncrypt,
                        email,
                        dateOfBirthday,
                        genre,
                        0,
                        "",
                        false,
                        null,
                        Setting(null, 10, 18, 30, genre, null),
                        Location(null, 0.0, 0.0,null)
                    )
                    runBlocking {
                        val crudApi = CrudApi()
                        val corrutina = launch {
                            userPosted = crudApi.addUserToAPI(user)
                        }
                        corrutina.join()
                        if (userPosted) {
                            Toast.makeText(
                                applicationContext,
                                "Usuari inserit amb èxit",
                                Toast.LENGTH_LONG
                            ).show()
                            Handler().postDelayed({
                                finish()
                            }, 3000)

                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Ja existeix un usuari amb aquest nom",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        println("Usuari inserit: " + userPosted.toString())
                    }
                } else {
                    Toast.makeText(this, "Les contrasenyes no coincideixen", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(this, "Hi han camps buits!!!", Toast.LENGTH_LONG).show()
            }
        }
    }
//    fun showDatePicker() {
//        // Se crea un Calendar con la fecha actual
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        val dateFormat = DateTimeFormatter.ofPattern("yyyy-M-dd")
//        // Se crea el DatePickerDialog
//        val datePickerDialog =
//            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
//                // Se establece la fecha seleccionada en el EditText
//                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
//                val sdf = LocalDate.parse(selectedDate,dateFormat)
//                binding.etBirthday.setText(sdf.toString())
//            }, year, month, day)
//
//        // Se muestra el DatePickerDialog
//        datePickerDialog.show()
//    }
    fun encryptPassword(input: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val messageDigest = md.digest(input.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }
            hashtext
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    fun putFullScreen() {
        this.supportActionBar?.hide()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
}