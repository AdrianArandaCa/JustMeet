package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justmeet.Adapters.AdapterAnswer
import com.example.justmeet.Models.listQuestion
import com.example.justmeet.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var adapter: AdapterAnswer
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()

        if (listQuestion != null) {
            setupViews()
            startTimer()
        }
        for (res in listQuestion){
            for (ans in res.answers) {
                if(ans.selected==1) {
                    println("Pregunta " +res.question1)
                    println("Respuesta seleccionada"+ans.answer1)

                }
            }
        }

    }

    private fun setupViews() {
        binding.numberQuestion.text = "Pregunta ${(currentQuestionIndex+1)}/${listQuestion.size}"
        binding.txtQuestion.text = listQuestion[currentQuestionIndex].question1
        adapter = AdapterAnswer(listQuestion[currentQuestionIndex].answers)
        binding.rvAnswers.adapter = adapter
        binding.rvAnswers.layoutManager = LinearLayoutManager(this)
    }

    private fun startTimer() {
        val countDownTimer = object: CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTime.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                currentQuestionIndex++
                if (currentQuestionIndex < listQuestion.size) {
                    binding.txtQuestion.text = listQuestion[currentQuestionIndex].question1
                    adapter = AdapterAnswer(listQuestion[currentQuestionIndex].answers)
                    binding.rvAnswers.adapter = adapter
                    binding.rvAnswers.layoutManager = LinearLayoutManager(applicationContext)
                    binding.numberQuestion.text = "Pregunta ${(currentQuestionIndex+1)}/${listQuestion.size}"
                    startTimer()
                }
            }
        }

        countDownTimer.start()
    }

    private fun putFullScreen() {
        supportActionBar?.hide()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}
