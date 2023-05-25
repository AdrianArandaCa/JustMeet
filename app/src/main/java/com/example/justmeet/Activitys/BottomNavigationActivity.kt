package com.example.justmeet.Activitys

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.isConnectedChat
import com.example.justmeet.Models.isDebug
import com.example.justmeet.Models.userLog
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityBottomNavigationBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.premiumFragment,
                R.id.playFragment,
                R.id.comunicationFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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

    override fun onDestroy() {
        super.onDestroy()
        userLog!!.isConnected = false
        runBlocking {
            val crudApi = CrudApi()
            val corrutina = launch {
                crudApi.modifyUserFromApi(userLog!!)
            }
            corrutina.join()
        }
        userLog = null
    }

    override fun onStop() {
        super.onStop()

        if(!isDebug) {
            userLog!!.isConnected = isConnectedChat
            runBlocking {
                val crudApi = CrudApi()
                val corrutina = launch {
                    crudApi.modifyUserFromApi(userLog!!)
                }
                corrutina.join()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if(!isDebug) {
            isConnectedChat = false
            userLog!!.isConnected = true
            runBlocking {
                val crudApi = CrudApi()
                val corrutina = launch {
                    crudApi.modifyUserFromApi(userLog!!)
                }
                corrutina.join()
            }
        }

    }
}