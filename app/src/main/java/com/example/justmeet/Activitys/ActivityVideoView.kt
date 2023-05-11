package com.example.justmeet.Activitys

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.MediaController
import com.example.justmeet.Models.Advertisement
import com.example.justmeet.Models.Avatar
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityVideoViewBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class ActivityVideoView : AppCompatActivity() {
    private lateinit var binding : ActivityVideoViewBinding
    private lateinit var advertisementList : ArrayList<Advertisement>
    private var currentPosition: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        listVideoViewsInitializer()
        val randomDelay = Random.nextInt(0, advertisementList.size)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.vvAnuncio)
        val videoPath = advertisementList[randomDelay].url
        var uri = Uri.parse(videoPath)
        binding.vvAnuncio.setMediaController(mediaController)
        binding.vvAnuncio.setVideoURI(uri)
        binding.vvAnuncio.requestFocus()
        binding.vvAnuncio.start()

        binding.btnGoWebsite.setImageResource(advertisementList[randomDelay].logo)

        binding.btnGoWebsite.setOnClickListener {
            openAdvertiserWebsite(advertisementList[randomDelay].website)
        }
        binding.vvAnuncio.setOnCompletionListener {

            val intento = Intent(this, BottomNavigationActivity::class.java)
            startActivity(intento)
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        currentPosition = binding.vvAnuncio.currentPosition
        binding.vvAnuncio.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.vvAnuncio.seekTo(currentPosition)
        binding.vvAnuncio.start()
    }
    private fun openAdvertiserWebsite(website : String) {
        val advertiserUri = Uri.parse(website)
        val intent = Intent(Intent.ACTION_VIEW, advertiserUri)
        startActivity(intent)
    }
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        // Evitar interacción táctil en toda la pantalla
//        return true
//    }
    fun putFullScreen() {
        this.supportActionBar?.hide()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
    private fun listVideoViewsInitializer(){

        advertisementList = arrayListOf()
        advertisementList.addAll(
            listOf(
                Advertisement(1,"Nesquik","android.resource://"+this.packageName+"/"+R.raw.anuncionesquik,"https://www.goodnes.com/nesquik/",R.drawable.nesquiklogo),
                Advertisement(2,"Coca Cola","android.resource://"+this.packageName+"/"+R.raw.anunciococacola,"https://www.cocacolaespana.es/",R.drawable.cocacolalogo),
                Advertisement(3,"Vanish Gold","android.resource://"+this.packageName+"/"+R.raw.anunciodetergente,"https://www.vanish.es/",R.drawable.vanish),
                Advertisement(4,"Paco Rabanne","android.resource://"+this.packageName+"/"+R.raw.anuncioinvictus,"https://www.pacorabanne.com/",R.drawable.pacorrabane),
                Advertisement(5,"Kalise","android.resource://"+this.packageName+"/"+R.raw.anunciokalise,"https://www.kalise.com/",R.drawable.kalise)
            )
        )
    }
}