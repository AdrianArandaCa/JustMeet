package com.example.justmeet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.justmeet.Models.gameFinishFromSocket
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityResumeIsMatchBinding

class ActivityResumeIsMatch : AppCompatActivity() {
    private lateinit var binding : ActivityResumeIsMatchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeIsMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        binding.tvPorcentajeNum.setText(gameFinishFromSocket.percentage.toString()+"%")

        binding.btnVerMatches.setOnClickListener {

            val intento = Intent(this, BottomNavigationActivity::class.java)
            startActivity(intento)
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
}