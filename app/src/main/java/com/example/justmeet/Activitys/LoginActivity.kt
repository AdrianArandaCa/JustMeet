package com.example.justmeet.Activitys

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.constraintlayout.motion.widget.Key.VISIBILITY
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.*
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityLoginBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.*
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityLoginBinding
    private var job: Job = Job()
    lateinit var user: User
    private lateinit var webSocket: WebSocket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        putFullScreen()
        getPermissionsApi()
        binding.progressBar.visibility = View.VISIBLE
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://172.16.24.123:45456/ws/2").build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)

            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val gson = Gson()
                val listType = object : TypeToken<List<Question>>() {}.type
                try {
                    runBlocking {
                        var corrutina = launch {
                            val data: List<Question> = gson.fromJson(text, listType)
                            for (question in data) {
                                println(question.question1)
                            }
                        }
                        corrutina.join()
                        runOnUiThread {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)

            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)

            }
        })

//        runBlocking {
//
//            val crudApi = CrudApi()
//            val corrutina = launch {
//                 llistaUsers  = crudApi.getAllUsersFromAPI()
//            }
//            corrutina.join()
//        }
//
//        for(user in llistaUsers) {
//            println(user.name)
//        }


        binding.btnRegister.setOnClickListener {

            val intento = Intent(this, RegisterActivity::class.java)
            startActivity(intento)
        }

        binding.btnLogin.setOnClickListener {
            val intento = Intent(this, BottomNavigationActivity::class.java)
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
            tmf = TrustManagerFactory.getInstance(algorithm)
            tmf.init(keyStore)
            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, tmf.trustManagers, null)

        } finally {
            inputStream.close()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


}