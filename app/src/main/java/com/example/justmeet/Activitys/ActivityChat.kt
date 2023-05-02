package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justmeet.Adapters.AdapterAnswer
import com.example.justmeet.Adapters.AdapterAvatar
import com.example.justmeet.Adapters.AdapterChat
import com.example.justmeet.Models.Chat
import com.example.justmeet.Models.User
import com.example.justmeet.Models.listChatUsers
import com.example.justmeet.R
import com.example.justmeet.Socket.MessageListener
import com.example.justmeet.Socket.WebSocketManager
import com.example.justmeet.databinding.ActivityChatBinding

class ActivityChat : AppCompatActivity(){
    private lateinit var binding : ActivityChatBinding
    private lateinit var adapter: AdapterChat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        //sendList()
        val userRecived = intent.getSerializableExtra("User") as? User
        binding.ivAvatarChat.setImageResource(userRecived?.photo!!)
        binding.tvNameUser.setText(userRecived.name)
        println("Usuario recibido: "+ userRecived?.name)
        if(!listChatUsers.isEmpty()){
            listChatUsers.clear()
        }
        adapter = AdapterChat(listChatUsers)
        binding.recyclerChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerChat.adapter = adapter
        binding.btnSendMessage.setOnClickListener {
            var message = binding.etSendMessage.text.toString()
            WebSocketManager.sendMessage(message)
            var chat = Chat(message,true)
            listChatUsers.add(chat)
            adapter.updateDades(listChatUsers)

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
}