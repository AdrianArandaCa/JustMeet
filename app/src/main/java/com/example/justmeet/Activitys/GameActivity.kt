package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justmeet.Adapters.AdapterAnswer
import com.example.justmeet.Models.listAnswer
import com.example.justmeet.Models.listQuestion
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityGameBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGameBinding
    private lateinit var adapter : AdapterAnswer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for(i in 0..listQuestion.size - 1) {
            Handler().postDelayed(
                {
                    binding.txtQuestion.setText(listQuestion[i].question1)
                    adapter = AdapterAnswer(listQuestion[i].answers)
                    binding.rvAnswers.adapter = adapter
                    binding.rvAnswers.layoutManager = LinearLayoutManager(this)

                }, 3000
            )
        }
    }
}