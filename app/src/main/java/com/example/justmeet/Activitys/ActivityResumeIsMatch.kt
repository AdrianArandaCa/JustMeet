package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityResumeIsMatchBinding

class ActivityResumeIsMatch : AppCompatActivity() {
    private lateinit var binding : ActivityResumeIsMatchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeIsMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}