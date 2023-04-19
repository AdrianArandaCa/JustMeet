package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityResumeNotMatchBinding

class ActivityResumeNotMatch : AppCompatActivity() {
    private lateinit var binding : ActivityResumeNotMatchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeNotMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}