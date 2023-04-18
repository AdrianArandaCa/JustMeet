package com.example.justmeet.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        var isMale : Boolean = false
        var isFemale : Boolean = true
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        binding.backButtonSettings.setOnClickListener {
            onBackPressed()
        }
        binding.rbMale.setOnClickListener {
            binding.rbFemale.isChecked = false
            isMale = true
            isFemale = false
        }
        binding.rbFemale.setOnClickListener {
            binding.rbMale.isChecked = false
            isFemale = true
            isMale = false
        }

        val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Obtiene los valores actuales de ambas SeekBar
                val minValue = binding.seekBarMin.progress
                val maxValue = binding.seekBarMax.progress

                if (seekBar == binding.seekBarMin) {
                    // Limita el valor máximo de la seekBarMin al valor actual de la seekBarMax
                    if (minValue > maxValue) {
                        binding.seekBarMin.progress = maxValue
                    }
                } else if (seekBar == binding.seekBarMax) {
                    // Limita el valor mínimo de la seekBarMax al valor actual de la seekBarMin
                    if (maxValue < minValue) {
                        binding.seekBarMax.progress = minValue
                    }
                }
                // Actualiza los valores en algún otro elemento de la interfaz de usuario, como un TextView
                if(maxValue> minValue && minValue < maxValue) {
                    binding.tvEdadMinMax.text = "$minValue-$maxValue"
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Este método se llama cuando el usuario comienza a interactuar con la SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Este método se llama cuando el usuario deja de interactuar con la SeekBar
            }
        }

// Asigna el objeto de tipo OnSeekBarChangeListener a ambas SeekBar
        binding.seekBarMin.setOnSeekBarChangeListener(seekBarChangeListener)
        binding.seekBarMax.setOnSeekBarChangeListener(seekBarChangeListener)
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