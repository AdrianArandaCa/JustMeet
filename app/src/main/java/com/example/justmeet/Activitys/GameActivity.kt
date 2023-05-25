package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Adapters.AdapterAnswer
import com.example.justmeet.Models.*
import com.example.justmeet.Socket.WebSocketManager
import com.example.justmeet.databinding.ActivityGameBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class GameActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityGameBinding
    private lateinit var adapter: AdapterAnswer
    private var currentQuestionIndex = 0
    private var listUserAnswer: List<UserAnswer> = listOf()
    var job = Job()
    var answerSelected: Boolean = false

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

    // Init and refresh recyclerview
    private fun setupViews() {
        binding.numberQuestion.text = "Pregunta ${(currentQuestionIndex + 1)}/${listQuestion.size}"
        binding.txtQuestion.text = listQuestion[currentQuestionIndex].question1
        adapter = AdapterAnswer(listQuestion[currentQuestionIndex].answers)
        binding.rvAnswers.adapter = adapter
        binding.rvAnswers.layoutManager = LinearLayoutManager(this)
    }

    // Start timer when game its initalitzed
    private fun startTimer() {
        // timeForGame =  Time for question
        val countDownTimer = object : CountDownTimer(timeForGame, 1000) {
            // Every second updated timer / textview
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTime.text = (millisUntilFinished / 1000).toString()
            }
            // If countdowntimer its 0/finish, reload the recyclerview with another question and reset timer
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
                    // Add answers selected into new list
                    for (question in listQuestion) {
                        answerSelected = false
                        for (answer in question.answers) {
                            if (answer.selected == 1) {
                                answerSelected = true
                                var userAns = UserAnswer(
                                    gameFromSocket.idGame!!,
                                    userLog!!.idUser!!,
                                    question.idQuestion,
                                    answer.idAnswer
                                )
                                listUserAnswer += userAns
                            }
                        }
                        // If user dont select an answer, then idAnswer is null
                        if (!answerSelected) {
                            var userAns = UserAnswer(
                                gameFromSocket.idGame!!,
                                userLog!!.idUser!!,
                                question.idQuestion,
                                null
                            )
                            listUserAnswer += userAns
                        }
                    }
                    // Add userAnswers to API
                    runBlocking {
                        val crudApi = CrudApi()
                        val corrutina = launch {
                            crudApi.addUserAnswerToAPI(listUserAnswer)
                        }
                        corrutina.join()
                    }
                    // Sleep main thread for avoid a collision with WebSocket
                    val randomDelay = Random.nextInt(600, 800)
                    Thread.sleep(randomDelay.toLong())
                    WebSocketManager.sendMessage("GAMERESULT${gameFromSocket.idGame}")
                    WebSocketManager.sendMessage("CLOSE")

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
        WebSocketManager.sendMessage("CLOSE")
    }

    override fun onStop() {
        super.onStop()
        WebSocketManager.sendMessage("CLOSE")
        finish()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}
