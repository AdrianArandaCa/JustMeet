package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justmeet.Adapters.AdapterAvatar
import com.example.justmeet.Models.Avatar
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivitySelectAvatarBinding

class ActivitySelectAvatar : AppCompatActivity() {
    private lateinit var binding : ActivitySelectAvatarBinding
    private lateinit var avatarList : ArrayList<Avatar>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAvatarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        listAvatarInitializer()
        llansarLlista()

    }
    private fun listAvatarInitializer(){

        avatarList = arrayListOf()
        avatarList.addAll(
            listOf(
                Avatar(1,R.drawable.logodoradouno,"Aguila"),
                Avatar(2,R.drawable.logodoradodos,"Calavera"),
                Avatar(3,R.drawable.logodoradotres,"Ciervo"),
                Avatar(4,R.drawable.logodoradocuatro,"CorazonCruz"),
                Avatar(5,R.drawable.logodoradocinco,"Leon"),
                Avatar(6,R.drawable.logodoradoseis,"Pistola"),
                )
        )
    }
    private fun llansarLlista() {
        binding.recyclerAvatares.layoutManager = GridLayoutManager(this,2)
        binding.recyclerAvatares.adapter = AdapterAvatar(avatarList)
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