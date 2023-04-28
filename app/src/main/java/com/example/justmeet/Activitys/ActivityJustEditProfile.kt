package com.example.justmeet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.Setting
import com.example.justmeet.Models.User
import com.example.justmeet.Models.userLog
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityJustEditProfileBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ActivityJustEditProfile : AppCompatActivity() {
    private lateinit var binding : ActivityJustEditProfileBinding
    var changeSaved : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJustEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        binding.ivProfile.setImageResource(userLog!!.photo!!)
        binding.tvSobreMiContent.setText(userLog!!.description)

        binding.tvCambiarAvatar.setOnClickListener {

            val intento = Intent(this, ActivitySelectAvatar::class.java)
            startActivity(intento)
        }

        binding.btnGuardarCambiosEdit.setOnClickListener {

            if(binding.tvSobreMiContent.text.toString().equals(userLog!!.description) ){

                Toast.makeText(this,"No hay cambios a modificar", Toast.LENGTH_LONG).show()
            } else {

                runBlocking {
                    val corrutina = launch {
                        val crudApi = CrudApi()
                        userLog!!.description = binding.tvSobreMiContent.text.toString()
                        changeSaved = crudApi.modifyUserFromApi(userLog!!)
                    }
                    corrutina.join()
                }

                Toast.makeText(this,"CAMBIOS GUARDADOS",Toast.LENGTH_LONG).show()
            }
        }
        binding.backButtonEdit.setOnClickListener {
            if(changeSaved) {
                onBackPressed()
                finish()

            }
            if(binding.tvSobreMiContent.text.toString().equals(userLog!!.description) ){
                onBackPressed()
                finish()
            }
            if(!binding.tvSobreMiContent.text.toString().equals(userLog!!.description) && !changeSaved
            ) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Salir del Perfil")
                builder.setMessage("¿Estás seguro de que deseas salir sin guardar cambios?")
                builder.setPositiveButton("Sí") { _, _ ->
                    // Agrega aquí el código para cerrar sesión
                    onBackPressed()
                    finish()
                }
                builder.setNegativeButton("No") { _, _ ->

                }
                val dialog = builder.create()
                dialog.show()
            }
        }


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