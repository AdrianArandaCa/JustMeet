package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityJustEditProfileBinding

class ActivityJustEditProfile : AppCompatActivity() {
    private lateinit var binding : ActivityJustEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJustEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}