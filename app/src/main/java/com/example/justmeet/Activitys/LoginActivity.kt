package com.example.justmeet.Activitys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.*
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityLoginBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    private var permisosGarantits: Boolean = false
    private val locationRequestCode = 0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationModified: Boolean = false
    val urlapi = "https://api.openrouteservice.org/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var isPressed = false
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        demanarPermisos()
        putFullScreen()
        getPermissionsApi()
        binding.btnRegister.setOnClickListener {

            val intento = Intent(this, RegisterActivity::class.java)
            startActivity(intento)
        }

        binding.btnLogin.setOnClickListener {
            isPressed = true
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
                if (passWord.equals(userLog!!.password)) {
                    runOnUiThread {
                        binding.progressBar.visibility = View.VISIBLE
                        Toast.makeText(this, "Login Correcte", Toast.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                    }
                    if (userLog!!.photo == 0) {
                        getLocationPermission(isPressed)
                        val intento = Intent(this, ActivitySelectAvatar::class.java)
                        startActivity(intento)
                    } else {
                        getLocationPermission(isPressed)
                        val intento = Intent(this, BottomNavigationActivity::class.java)
                        startActivity(intento)
                    }


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

    fun demanarPermisos() {
        if (
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            permisosGarantits = true

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Toast.makeText(
                    this,
                    "El permís ACCESS_FINE_LOCATION no està disponible",
                    Toast.LENGTH_LONG
                ).show()
                permisosGarantits = false
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    Toast.makeText(
                        this,
                        "El permís ACCESS_COARSE_LOCATION no està disponible",
                        Toast.LENGTH_LONG
                    ).show()
                    permisosGarantits = false
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        locationRequestCode
                    )
                }
            }
        }

    }

    fun getLocationPermission(isPressed: Boolean) {
        if (
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            permisosGarantits = true
            if (isPressed) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (userLog != null) {
                        var userLocation: com.example.justmeet.Models.Location =
                            com.example.justmeet.Models.Location(
                                null, location!!.longitude, location!!.latitude,
                                userLog!!.idUser!!
                            )
                        userLog!!.location = userLocation
                        runBlocking {
                            var corrutina = launch {
                                val crudApi = CrudApi()
                                locationModified = crudApi.modifyLocationUser(userLocation)

                            }
                            corrutina.join()

                        }
                        if (locationModified) {
                            println("LOCALIZACION USUARIO MODIFICADA")
                        }
                        //FALTA CORRUTINA
                        // PUT LOCATION
                        // Hacer lo mismo pero con POST en el Register
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (locationRequestCode == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                // permisosGarantits = true
                getLocationPermission(false)
            } else
                permisosGarantits = false
        }
    }

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
}