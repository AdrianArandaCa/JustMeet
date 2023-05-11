package com.example.justmeet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Adapters.AdapterAvatar
import com.example.justmeet.Models.Avatar
import com.example.justmeet.Models.userLog
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityBottomNavigationBinding
import com.example.justmeet.databinding.ActivitySelectAvatarBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ActivitySelectAvatar : AppCompatActivity(),CoroutineScope {
    private lateinit var binding : ActivitySelectAvatarBinding
    private lateinit var avatarList : ArrayList<Avatar>
    private var isSelected : Boolean = false
    var job = Job()
    private var userModified : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectAvatarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        listAvatarInitializer()
        sendList()

        binding.btnGuardarAvatar.setOnClickListener {
            isSelected = false
            for(i in 0..avatarList.size-1) {
                if(avatarList[i].selected==1) {
                    userLog!!.photo = avatarList[i].resourcePhoto
                    isSelected = true
                }
            }
            if(!isSelected){
                Toast.makeText(this,getString(R.string.select_an_avatar),Toast.LENGTH_LONG).show()
            } else {
                runBlocking {
                    val corrutina = launch {
                        val crudApi = CrudApi()
                        userModified=  crudApi.modifyUserFromApi(userLog!!)
                    }
                    corrutina.join()
                }
                if(userModified) {
                    val intento = Intent(this,BottomNavigationActivity::class.java)
                    startActivity(intento)
                } else {
                    Toast.makeText(this,getString(R.string.insert_error_avatar),Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    private fun listAvatarInitializer(){

        avatarList = arrayListOf()
        avatarList.addAll(
            listOf(
                Avatar(1,R.drawable.avatargoldman,"Aguila",0),
                Avatar(2,R.drawable.avatargoldmale,"Calavera",0),
                Avatar(3,R.drawable.avatargoldmaletwo,"Ciervo",0),
                Avatar(4,R.drawable.avatargoldmantwo,"CorazonCruz",0),
                Avatar(5,R.drawable.avatargoldmanthree,"Leon",0),
                Avatar(6,R.drawable.avatargoldnobinario,"Pistola",0),
                )
        )
    }
    private fun sendList() {
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}