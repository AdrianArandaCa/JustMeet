package com.example.justmeet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.*
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityLoginBinding
import kotlinx.coroutines.*
import okhttp3.*
import java.io.InputStream
import java.math.BigInteger
import java.security.KeyStore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
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
        binding.btnRegister.setOnClickListener {

            val intento = Intent(this, RegisterActivity::class.java)
            startActivity(intento)
        }

        binding.btnLogin.setOnClickListener {
//            val intento = Intent(this, BottomNavigationActivity::class.java)
//            startActivity(intento)
            var userName = binding.etUserName.text.toString()

            runBlocking {
                val crudApi = CrudApi()
                val corrutina = launch {
                    userLog = crudApi.getOneUserByName(userName)
                }
                corrutina.join()
            }

            if (userLog != null) {
                var passWord = encryptPassword(binding.etPassword.text.toString())
                if (passWord.equals(userLog.password)) {
                    runOnUiThread {
                        binding.progressBar.visibility = View.VISIBLE
                        Toast.makeText(this, "Login Correcte", Toast.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                    }
                    Handler().postDelayed({
                        val intento = Intent(this, BottomNavigationActivity::class.java)
                        startActivity(intento)
                    }, 0)

                } else {
                    Toast.makeText(this, "Contrasenya incorrecte", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(this, "Aquest nom d'usuari no està registrat", Toast.LENGTH_LONG)
                    .show()
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

    fun encryptPassword(input: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val messageDigest = md.digest(input.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }
            hashtext
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
}