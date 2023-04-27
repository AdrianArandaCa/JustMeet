package com.example.justmeet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.justmeet.Activitys.LoginActivity
import com.example.justmeet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        putFullScreen()
        binding.ivLogoSplash.animate()
            .scaleX(3.0f)
            .scaleY(3.0f)
            .setDuration(2000)
            .start()


        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3500)
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
}