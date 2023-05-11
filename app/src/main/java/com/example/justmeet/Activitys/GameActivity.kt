package com.example.justmeet.Activitys

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Adapters.AdapterAnswer
import com.example.justmeet.Models.*
import com.example.justmeet.Socket.MessageListener
import com.example.justmeet.Socket.WebSocketManager
import com.example.justmeet.databinding.ActivityGameBinding
import com.example.justmeet.databinding.ActivityResumeNotMatchBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class GameActivity : AppCompatActivity(), CoroutineScope{

    private lateinit var binding: ActivityGameBinding
    private lateinit var adapter: AdapterAnswer
    private var currentQuestionIndex = 0
    private var listUserAnswer: List<UserAnswer> = listOf()
    var job = Job()
    var answerSelected : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()


        if (listQuestion != null) {
            setupViews()
            startTimer()
        }
    }

    private fun setupViews() {
        binding.numberQuestion.text = "Pregunta ${(currentQuestionIndex + 1)}/${listQuestion.size}"
        binding.txtQuestion.text = listQuestion[currentQuestionIndex].question1
        adapter = AdapterAnswer(listQuestion[currentQuestionIndex].answers)
        binding.rvAnswers.adapter = adapter
        binding.rvAnswers.layoutManager = LinearLayoutManager(this)
    }

    private fun startTimer() {
        val countDownTimer = object : CountDownTimer(5000, 1000) {
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
                    binding.numberQuestion.text =
                        "Pregunta ${(currentQuestionIndex + 1)}/${listQuestion.size}"
                    startTimer()
                } else {
                    println("ELSE")
                    for (question in listQuestion) {
                        answerSelected = false
                        for (answer in question.answers) {
                            if (answer.selected == 1) {
                                println("Respuesta  seleccionada")
                                answerSelected = true
                                var userAns = UserAnswer(
                                    gameFromSocket.idGame!!,
                                    userLog!!.idUser!!,
                                    question.idQuestion,
                                    answer.idAnswer
                                )
                                listUserAnswer += userAns

                                println("Pregunta " + question.question1)
                                println("Respuesta seleccionada" + answer.answer1)
                            }
                        }
                        if(!answerSelected) {
                            println("Respuesta no seleccionada")
                            var userAns = UserAnswer(
                                gameFromSocket.idGame!!,
                                userLog!!.idUser!!,
                                question.idQuestion,
                                null
                            )
                            listUserAnswer += userAns
                        }
                    }
                    println("estamos")
                    runBlocking {
                        val crudApi = CrudApi()
                        val corrutina = launch {
                            crudApi.addUserAnswerToAPI(listUserAnswer)
                        }
                        corrutina.join()
                    }
                    println("USER ANSWER INSERIT!!!")
                    val randomDelay = Random.nextInt(600, 800)
                    Thread.sleep(randomDelay.toLong())
                            WebSocketManager.sendMessage("GAMERESULT${gameFromSocket.idGame}")
                    println("FINAL WEBSOCKET")
                    WebSocketManager.sendMessage("CLOSE")
                    println("PASSA POR CLOSE")

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


    override fun onDestroy() {
        super.onDestroy()
        //WebSocketManager.sendMessage("CLOSE")
    }
    override fun onStop() {
        super.onStop()
        WebSocketManager.sendMessage("CLOSE")
        finish()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}
