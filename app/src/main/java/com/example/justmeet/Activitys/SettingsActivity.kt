package com.example.justmeet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.Setting
import com.example.justmeet.Models.isDebug
import com.example.justmeet.Models.userLog
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivitySettingsBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SettingsActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var userSetting: Setting
    var job = Job()
    var changeSaved: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        if (!isDebug) {
            runBlocking {
                val corrutina = launch {
                    val crudapi = CrudApi()
                    userSetting = crudapi.getSettingById(userLog!!.idSetting!!)!!
                }
                corrutina.join()
            }
        } else {
            userSetting = userLog!!.idSettingNavigation!!
        }

        binding.seekbarDistancia.progress = userSetting.maxDistance
        binding.tvKM.setText(userSetting.maxDistance.toString() + "km")
        binding.seekBarMin.progress = userSetting.minAge
        binding.seekBarMax.progress = userSetting.maxAge
        binding.tvEdadMinMax.setText(userSetting.minAge.toString() + "-" + userSetting.maxAge.toString())
        if (userSetting.genre.equals("M")) {
            binding.rbMale.isChecked = true
        } else {
            binding.rbFemale.isChecked = true
        }

        binding.backButtonSettings.setOnClickListener {
            if (changeSaved) {
                onBackPressed()
                finish()
            }
            if (binding.seekbarDistancia.progress == userSetting.maxDistance && binding.seekBarMin.progress == userSetting.minAge &&
                binding.seekBarMax.progress == userSetting.maxAge
            ) {
                onBackPressed()
                finish()
            }
            if ((binding.seekbarDistancia.progress != userSetting.maxDistance || binding.seekBarMin.progress != userSetting.minAge ||
                        binding.seekBarMax.progress != userSetting.maxAge) && !changeSaved
            ) {
                // When user want to leave a activity without save changes
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.go_out_settings))
                builder.setMessage(getString(R.string.confirm_go_out_settings))
                builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    onBackPressed()
                    finish()
                }
                builder.setNegativeButton(getString(R.string.no)) { _, _ ->

                }
                val dialog = builder.create()
                dialog.show()
            }

        }
        binding.rbMale.setOnClickListener {
            binding.rbFemale.isChecked = false

        }
        binding.rbFemale.setOnClickListener {
            binding.rbMale.isChecked = false

        }
        binding.btnGuardarCambios.setOnClickListener {
            var genre = ""
            if (binding.rbMale.isChecked) {
                genre = "M"
            } else {
                genre = "F"
            }
            if (binding.seekbarDistancia.progress == userSetting.maxDistance && binding.seekBarMin.progress == userSetting.minAge &&
                binding.seekBarMax.progress == userSetting.maxAge && userSetting.genre.equals(genre)
            ) {

                Toast.makeText(this, getString(R.string.nochangesmodify), Toast.LENGTH_LONG).show()
            } else {
                var genre = ""
                if (binding.rbMale.isChecked) {
                    genre = "M"
                } else {
                    genre = "F"
                }
                runBlocking {
                    val corrutina = launch {
                        val crudApi = CrudApi()
                        var setting = Setting(
                            userLog!!.idSetting!!,
                            binding.seekbarDistancia.progress,
                            binding.seekBarMin.progress,
                            binding.seekBarMax.progress,
                            genre,
                            2,
                            null
                        )
                        changeSaved = crudApi.modifySettingFromApi(setting)
                    }
                    corrutina.join()
                }
                Toast.makeText(this, getString(R.string.savechanges), Toast.LENGTH_LONG).show()
            }
        }
        binding.btnCerrarSesion.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.close_session))
            builder.setMessage(getString(R.string.confirm_close_session))
            builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                userLog!!.isConnected = false
                runBlocking {
                    val crudApi = CrudApi()
                    val corrutina = launch {
                        crudApi.modifyUserFromApi(userLog!!)
                    }
                    corrutina.join()
                }
                userLog = null
                val intento = Intent(this, LoginActivity::class.java)
                startActivity(intento)
                finish()
                Toast.makeText(this, getString(R.string.close_session), Toast.LENGTH_LONG).show()
            }
            builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            }
            val dialog = builder.create()
            dialog.show()
        }

        // Check seekbar values
        val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val minValue = binding.seekBarMin.progress
                val maxValue = binding.seekBarMax.progress
                val maxDistanca = binding.seekbarDistancia.progress

                if (seekBar == binding.seekBarMin) {
                    if (minValue > maxValue) {
                        binding.seekBarMin.progress = maxValue
                    }
                } else if (seekBar == binding.seekBarMax) {
                    if (maxValue < minValue) {
                        binding.seekBarMax.progress = minValue
                    }
                } else if (seekBar == binding.seekbarDistancia) {
                    binding.seekbarDistancia.progress = maxDistanca
                }
                if (maxValue > minValue && minValue < maxValue) {
                    binding.tvEdadMinMax.text = "$minValue-$maxValue"
                }
                binding.tvKM.text = "$maxDistanca km."
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }

        binding.seekBarMin.setOnSeekBarChangeListener(seekBarChangeListener)
        binding.seekBarMax.setOnSeekBarChangeListener(seekBarChangeListener)
        binding.seekbarDistancia.setOnSeekBarChangeListener(seekBarChangeListener)
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