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
        binding.planSelected.setText(getString(R.string.plan_selected, monthSelected, priceSelected))
        binding.btnConfirmBuyPremium.setOnClickListener {
            var numberTarget = binding.etTargetNumber.text.toString()
            var nameSurnameTarget = binding.etNameSurNameTarget.text.toString()
            var mmaa = binding.etMMAA.text.toString()
            var cvc = binding.etCVC.text.toString()
            var postalcode = binding.etPostalCode.text.toString()
            var email = binding.etEmail.text.toString()

            if (numberTarget.isNotEmpty() && nameSurnameTarget.isNotEmpty() && mmaa.isNotEmpty() && cvc.isNotEmpty() && postalcode.isNotEmpty() && email.isNotEmpty()) {

                if(!mmaa.contains("/")) {
                    Toast.makeText(this,"getString(R.string.mmaa_error)",Toast.LENGTH_LONG).show()
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
                        Toast.makeText(this,getString(R.string.premium_comprado),Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this,getString(R.string.empty_information),Toast.LENGTH_LONG).show()
            }
        }
        binding.ibBack.setOnClickListener {
            onBackPressed()
            finish()
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


}