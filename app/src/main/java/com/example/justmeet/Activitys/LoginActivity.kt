package com.example.justmeet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()

        binding.btnRegister.setOnClickListener {

            val intento = Intent(this,RegisterActivity::class.java)
            startActivity(intento)
        }

        binding.btnLogin.setOnClickListener {
            val intento = Intent(this,BottomNavigationActivity::class.java)
            startActivity(intento)
        }
    }
}