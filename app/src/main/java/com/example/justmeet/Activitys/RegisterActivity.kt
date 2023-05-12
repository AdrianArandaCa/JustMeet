package com.example.justmeet.Activitys

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.Location
import com.example.justmeet.Models.Setting
import com.example.justmeet.Models.User
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityRegisterBinding
import kotlinx.coroutines.*
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext


class RegisterActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityRegisterBinding
    var job = Job()
    var userPosted: Boolean = false
    var locationPosted: Boolean = false
    var isMale: Boolean = false
    var isFemale: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {

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
            registerUser()
        }
        binding.etUserName.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // Ocultar el teclado
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etUserName.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.etPassword.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // Ocultar el teclado
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etPassword.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.etPasswordConfirm.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // Ocultar el teclado
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etPasswordConfirm.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.etEmail.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // Ocultar el teclado
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etEmail.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.etBirthday.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // Ocultar el teclado
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etBirthday.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
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

    fun registerUser() {
        if (binding.etBirthday.text.toString() < "18" || binding.etBirthday.text.toString() > "99") {
            Toast.makeText(this, getString(R.string.invalid_age), Toast.LENGTH_LONG)
                .show()
        }
        var nomUser = binding.etUserName.text.toString()
        var passWord = binding.etPassword.text.toString()
        var passWordConfirm = binding.etPasswordConfirm.text.toString()
        var email = binding.etEmail.text.toString()
        var dateOfBirthday = binding.etBirthday.text.toString()
        //val sqlDate = java.sql.Date(dateOfBirthday.time)


        var genre = ""
        if (isMale) {
            genre = "M"
        } else if (isFemale) {
            genre = "F"
        }

        if (nomUser.isNotEmpty() && passWord.isNotEmpty() && passWordConfirm.isNotEmpty() && email.isNotEmpty() && binding.etBirthday.text.isNotEmpty() && genre.isNotEmpty()) {
            if (!isValidEmail(binding.etEmail.text.toString())) {
                Toast.makeText(this, getString(R.string.write_valid_email), Toast.LENGTH_LONG)
                    .show()
            } else {
                if (passWord.equals(passWordConfirm)) {
                    //Guardas usuario
                    var passWordEncrypt = encryptPassword(passWord)
                    var user = User(
                        null,
                        nomUser,
                        passWordEncrypt,
                        email,
                        dateOfBirthday.toInt(),
                        genre,
                        "0",
                        "",
                        false,
                        null,
                        Setting(null, 10, 18, 30, genre, 2, null),
                        Location(null, 0.0, 0.0, null)
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
                                getString(R.string.userregistredfine),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()


                        } else {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.userexistsbd),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        println("Usuari inserit: " + userPosted.toString())
                        runBlocking {
                            val corrutina = launch {
                                val crudApi = CrudApi()
                                var userName = crudApi.getOneUserByName(nomUser)
                                var locationNewUser = Location(null, 0.0, 0.0, userName!!.idUser)
                                locationPosted = crudApi.postLocation(locationNewUser)
                            }
                            corrutina.join()
                        }
                        println("Localització inserida " + locationPosted.toString())

                    }
                } else {
                    Toast.makeText(this, getString(R.string.differentpassword), Toast.LENGTH_LONG)
                        .show()
                }
            }

        } else {
            Toast.makeText(this, getString(R.string.empty_information), Toast.LENGTH_LONG).show()
        }
    }

    fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }


    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
}