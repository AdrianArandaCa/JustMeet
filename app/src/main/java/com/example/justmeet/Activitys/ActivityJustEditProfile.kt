package com.example.justmeet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.userLog
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityJustEditProfileBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ActivityJustEditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityJustEditProfileBinding
    var changeSaved: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJustEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        Glide.with(this).load(userLog!!.photo!!).into(binding.ivProfile)
        binding.tvSobreMiContent.setText(userLog!!.description)
        binding.tvCambiarAvatar.setOnClickListener {
            val intento = Intent(this, ActivitySelectAvatar::class.java)
            startActivity(intento)
            finish()
        }

        binding.btnGuardarCambiosEdit.setOnClickListener {
            if (binding.tvSobreMiContent.text.toString().equals(userLog!!.description)) {
                Toast.makeText(this, getString(R.string.nochangesmodify), Toast.LENGTH_LONG).show()
            } else {
                runBlocking {
                    val corrutina = launch {
                        val crudApi = CrudApi()
                        userLog!!.description = binding.tvSobreMiContent.text.toString()
                        changeSaved = crudApi.modifyUserFromApi(userLog!!)
                    }
                    corrutina.join()
                }
                Toast.makeText(this, getString(R.string.savechanges), Toast.LENGTH_LONG).show()
            }
        }

        binding.backButtonEdit.setOnClickListener {
            if (changeSaved) {
                onBackPressed()
                finish()
            }
            if (binding.tvSobreMiContent.text.toString().equals(userLog!!.description)) {
                onBackPressed()
                finish()
            }
            if (!binding.tvSobreMiContent.text.toString()
                    .equals(userLog!!.description) && !changeSaved
            ) {
                // When user want to leave a activity without save changes
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.go_out_profile))
                builder.setMessage(getString(R.string.confirm_go_out_profile))
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
    }

    // Full screen window
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