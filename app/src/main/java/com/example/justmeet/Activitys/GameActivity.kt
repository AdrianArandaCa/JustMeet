package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
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
    lateinit var listQuestion2 : ArrayList<Question>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtQuestion.setText(listQuestion[0].question1)
        adapter = AdapterAnswer(listQuestion[0].answers)
        binding.rvAnswers.adapter = adapter
        binding.rvAnswers.layoutManager = LinearLayoutManager(this)
//        for (i in 1.. listQuestion.size-1) {
//            val countDownTimer = object: CountDownTimer(10000, 1000) {
//                override fun onTick(millisUntilFinished: Long) {
//                    val seconds = millisUntilFinished / 1000
//                    binding.tvTime.text = seconds.toString()
//
//                }
//
//                override fun onFinish() {
//                    binding.txtQuestion.setText(listQuestion[i].question1)
//                    adapter = AdapterAnswer(listQuestion[i].answers)
//                    binding.rvAnswers.adapter = adapter
//                    binding.rvAnswers.layoutManager = LinearLayoutManager(applicationContext)
//                }
//            }
//
//            countDownTimer.start()
//        }





        //for(i in 0..listQuestion.size - 1) {





        //}
    }
}