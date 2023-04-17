package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justmeet.Adapters.AdapterAnswer
import com.example.justmeet.Models.Question
import com.example.justmeet.Models.listAnswer
import com.example.justmeet.Models.listQuestion
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityGameBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGameBinding
    private lateinit var adapter : AdapterAnswer
    private var currentQuestionIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        if (listQuestion != null) {
            binding.numberQuestion.setText("Pregunta ${(currentQuestionIndex+1)}/${listQuestion.size}")
            binding.txtQuestion.text = listQuestion[currentQuestionIndex].question1
            adapter = AdapterAnswer(listQuestion[currentQuestionIndex].answers)
            binding.rvAnswers.adapter = adapter
            binding.rvAnswers.layoutManager = LinearLayoutManager(this)

            // Se implementa el temporizador
            val countDownTimer = object: CountDownTimer(4000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = millisUntilFinished / 1000
                    binding.tvTime.text = seconds.toString()
                }

                override fun onFinish() {
                    // Se actualiza la pregunta y las respuestas
                    currentQuestionIndex++
                    if (currentQuestionIndex < listQuestion.size) {
                        binding.txtQuestion.text = listQuestion[currentQuestionIndex].question1
                        adapter = AdapterAnswer(listQuestion[currentQuestionIndex].answers)
                        binding.rvAnswers.adapter = adapter
                        binding.rvAnswers.layoutManager = LinearLayoutManager(applicationContext)

                        // Se reinicia el temporizador
                        binding.numberQuestion.setText("Pregunta ${(currentQuestionIndex+1)}/${listQuestion.size}")
                        startTimer()
                    }
                }
            }

            // Se inicia el temporizador por primera vez
            countDownTimer.start()
        }

    }
    private fun startTimer() {
        val countDownTimer = object: CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.tvTime.text = seconds.toString()
            }

            override fun onFinish() {
                // Se actualiza la pregunta y las respuestas
                currentQuestionIndex++
                if (currentQuestionIndex < listQuestion.size) {
                    binding.txtQuestion.text = listQuestion[currentQuestionIndex].question1
                    adapter = AdapterAnswer(listQuestion[currentQuestionIndex].answers)
                    binding.rvAnswers.adapter = adapter
                    binding.rvAnswers.layoutManager = LinearLayoutManager(applicationContext)

                    // Se reinicia el temporizador
                    binding.numberQuestion.setText("Pregunta ${(currentQuestionIndex+1)}/${listQuestion.size}")
                    startTimer()
                }
            }
        }

        countDownTimer.start()
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