package com.example.justmeet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.*
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityLoginBinding
import kotlinx.coroutines.*
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding : ActivityLoginBinding
    private var job: Job = Job()
    lateinit var user : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        getPermissionsApi()

        runBlocking {

            val crudApi = CrudApi()
            val corrutina = launch {
                 llistaUsers  = crudApi.getAllUsersFromAPI()
            }
            corrutina.join()
        }

        for(user in llistaUsers) {
            println(user.name)
        }





        binding.btnRegister.setOnClickListener {

            val intento = Intent(this,RegisterActivity::class.java)
            startActivity(intento)
        }

        binding.btnLogin.setOnClickListener {
            val intento = Intent(this,BottomNavigationActivity::class.java)
            startActivity(intento)
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
    fun getPermissionsApi() {
        val inputStream: InputStream = resources.openRawResource(R.raw.certificadoserver)

        // Cargar certificado
        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
        var ca: Certificate
        try {
            ca = cf.generateCertificate(inputStream)
            val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)
            algorithm = TrustManagerFactory.getDefaultAlgorithm()
            tmf= TrustManagerFactory.getInstance(algorithm)
            tmf.init(keyStore)
            sslContext= SSLContext.getInstance("TLS")
            sslContext.init(null, tmf.trustManagers, null)

        } finally {
            inputStream.close()
        }
    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


}