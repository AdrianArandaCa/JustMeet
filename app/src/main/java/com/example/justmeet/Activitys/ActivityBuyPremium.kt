package com.example.justmeet.Activitys

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.userLog
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityBuyPremiumBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ActivityBuyPremium : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityBuyPremiumBinding
    var job = Job()
    var userBuyedPremium: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        val priceSelected = intent.getStringExtra("Price")
        val monthSelected = intent.getStringExtra("Month")
        binding.planSelected.setText(
            getString(
                R.string.plan_selected,
                monthSelected,
                priceSelected
            )
        )
        // Hidden Keyboard when user press ENTER
        binding.etTargetNumber.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etTargetNumber.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.etNameSurNameTarget.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etNameSurNameTarget.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.etMMAA.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etMMAA.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.etCVC.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etCVC.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.etPostalCode.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etPostalCode.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.etEmail.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etEmail.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        // Check when user buy premium
        binding.btnConfirmBuyPremium.setOnClickListener {
            var numberTarget = binding.etTargetNumber.text.toString()
            var nameSurnameTarget = binding.etNameSurNameTarget.text.toString()
            var mmaa = binding.etMMAA.text.toString()
            var cvc = binding.etCVC.text.toString()
            var postalcode = binding.etPostalCode.text.toString()
            var email = binding.etEmail.text.toString()

            if (numberTarget.isNotEmpty() && nameSurnameTarget.isNotEmpty() && mmaa.isNotEmpty() && cvc.isNotEmpty() && postalcode.isNotEmpty() && email.isNotEmpty()) {

                if (!mmaa.contains("/")) {
                    Toast.makeText(this, getString(R.string.mmaa_restring), Toast.LENGTH_LONG)
                        .show()
                } else {
                    runBlocking {
                        val corrutina = launch {
                            val crudApi = CrudApi()
                            userLog!!.premium = true
                            userBuyedPremium = crudApi.modifyUserFromApi(userLog!!)
                        }
                        corrutina.join()
                    }
                    if (userBuyedPremium) {
                        onBackPressed()
                        finish()
                        Toast.makeText(
                            this,
                            getString(R.string.premium_comprado),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.empty_information), Toast.LENGTH_LONG)
                    .show()
            }
        }
        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    // Full screen window
    fun putFullScreen() {
        this.supportActionBar?.hide()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}