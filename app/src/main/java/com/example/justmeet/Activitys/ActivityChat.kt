package com.example.justmeet.Activitys

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.justmeet.Adapters.AdapterChat
import com.example.justmeet.Models.Chat
import com.example.justmeet.Models.User
import com.example.justmeet.Models.listChatUsers
import com.example.justmeet.Models.userLog
import com.example.justmeet.R
import com.example.justmeet.Socket.MessageListener
import com.example.justmeet.Socket.WebSocketManager
import com.example.justmeet.databinding.ActivityChatBinding
import com.google.gson.Gson
import kotlin.concurrent.thread
import kotlin.random.Random

class ActivityChat : AppCompatActivity(), MessageListener {
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: AdapterChat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        val userRecived = intent.getSerializableExtra("User") as? User
        val serverUrl = "ws://172.16.24.24:45456/ws/${userLog!!.idUser}"
        thread {
            kotlin.run {
                WebSocketManager.init(serverUrl, this)
                WebSocketManager.connect()
                val randomDelay = Random.nextInt(400, 500)
                Thread.sleep(randomDelay.toLong())
                WebSocketManager.sendMessage("CHAT${userRecived?.idUser.toString()}")
            }
        }
        //sendList()
        Glide.with(this).load(userRecived?.photo!!).into(binding.ivAvatarChat)
        // binding.ivAvatarChat.setImageResource(userRecived?.photo!!)
        binding.tvNameUser.setText(userRecived.name)
        println("Usuario recibido: " + userRecived?.name)
        if (!listChatUsers.isEmpty()) {
            listChatUsers.clear()
        }
        adapter = AdapterChat(listChatUsers)
        binding.recyclerChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerChat.adapter = adapter
        binding.btnSendMessage.setOnClickListener {
            var message = binding.etSendMessage.text.toString()
            if (message.isEmpty()) {
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                val duration: Long = 200 //
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(duration)
                }
            } else {
                WebSocketManager.sendMessage(message)
                var chat = Chat(message, 1)
                listChatUsers.add(chat)
                adapter.updateDades(listChatUsers)
                binding.etSendMessage.setText("")
                scrollToBottom()
            }
        }
        binding.btnBackChat.setOnClickListener {
            WebSocketManager.sendMessage("CLOSE")
            onBackPressed()
            finish()
        }
        binding.etSendMessage.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // Ocultar el teclado
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSendMessage.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun sendList() {
        binding.recyclerChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerChat.adapter = AdapterChat(listChatUsers)
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

    override fun onConnectSuccess() {
        println("CONNECT SUCCES CHAT")
    }

    override fun onConnectFailed() {
        println("CONNECT FAILED CHAT")
    }

    override fun onClose() {
        println("CONNECT CLOSE CHAT")
    }

    override fun onMessage(text: String?) {
        val gson = Gson()

        if (text != null) {
            if (text.startsWith("CLOSE")) {
                thread {
                    kotlin.run {
                        WebSocketManager.sendMessage("CLOSE")
                    }
                }
            } else if (text.startsWith("USERLEAVE")) {
                var textSubstring = text.substring(9)
                var chat = Chat(getString(R.string.user_leave_chat, textSubstring), 2)
                listChatUsers.add(chat)
                runOnUiThread {
                    adapter.updateDades(listChatUsers)
                }

            } else if (text.startsWith("USERCONNECT")) {
                var textSubstring = text.substring(11)
                var chat = Chat(getString(R.string.user_connect_chat, textSubstring), 2)
                listChatUsers.add(chat)
                runOnUiThread {
                    adapter.updateDades(listChatUsers)
                }
            } else {
                var chat = Chat(text, 0)
                listChatUsers.add(chat)
                runOnUiThread {
                    adapter.updateDades(listChatUsers)
                    scrollToBottom()
                }

            }
        }
    }

    private fun scrollToBottom() {
        binding.recyclerChat.postDelayed({
            binding.recyclerChat.smoothScrollToPosition(adapter.itemCount - 1)
        }, 100)
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.sendMessage("CLOSE")
    }

    override fun onStop() {
        super.onStop()
        WebSocketManager.sendMessage("CLOSE")
    }
}