package com.example.justmeet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
        binding.planSelected.setText("Plan seleccionado: ${monthSelected},${priceSelected}")
        binding.btnConfirmBuyPremium.setOnClickListener {
            var numberTarget = binding.etTargetNumber.text.toString()
            var nameSurnameTarget = binding.etNameSurNameTarget.text.toString()
            var mmaa = binding.etMMAA.text.toString()
            var cvc = binding.etCVC.text.toString()
            var postalcode = binding.etPostalCode.text.toString()
            var email = binding.etEmail.text.toString()

            if (numberTarget.isNotEmpty() && nameSurnameTarget.isNotEmpty() && mmaa.isNotEmpty() && cvc.isNotEmpty() && postalcode.isNotEmpty() && email.isNotEmpty()) {


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
                    Toast.makeText(this,"Premium comprado",Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.etMMAA.addTextChangedListener(object : TextWatcher {
            private var isDeleting = false

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 0 && before == 1 && s[start] == '/') {
                    isDeleting = true
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (isDeleting) {
                    isDeleting = false
                    return
                }

                val input = s.toString()
                if (input.length == 3 && input[2] != '/') {
                    val newInput = StringBuilder(input).insert(2, "/").toString()
                    binding.etMMAA.setText(newInput)
                    binding.etMMAA.setSelection(newInput.length)
                } else if (input.length == 2 && !input.contains("/")) {
                    binding.etMMAA.setText("$input/")
                    binding.etMMAA.setSelection(binding.etMMAA.text.length)
                }
            }
        })

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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


}