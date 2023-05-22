package com.example.justmeet.Activitys

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
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
    var contDebug = 0

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

        binding.etUserName.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // Ocultar el teclado
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etUserName.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.etPassword.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // Ocultar el teclado
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etPassword.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.ivLogoLogin.setOnClickListener {
            contDebug++
            if(contDebug == 5) {
                if (!isDebug){
                    binding.btnDebug.visibility = View.VISIBLE
                    isDebug = true
                } else {
                    binding.btnDebug.visibility = View.GONE
                    isDebug = false
                    userLog = null
                }
                contDebug = 0
            }



        }
        binding.btnDebug.setOnClickListener {
            userLog = User(
                1,
                "Debug",
                "1",
                "debug@gmail.com",
                22,
                "M",
                "https://cdn.create.vista.com/api/media/medium/471120374/stock-vector-beauty-gold-plated-metalic-icon?token=",
                "",
                false,
                1,
                Setting(1, 10, 18, 30, "M", 2, null),
                Location(1, 0.0, 0.0, 1),false
            )
            val intento = Intent(this, BottomNavigationActivity::class.java)
            startActivity(intento)
            finish()

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
                userLog!!.isConnected = true
                runBlocking {
                    val crudApi = CrudApi()
                    val corrutina = launch {
                        crudApi.modifyUserFromApi(userLog!!)
                    }
                    corrutina.join()
                }
                var passWord = encryptPassword(binding.etPassword.text.toString())
                if (passWord.equals(userLog!!.password) && userName.isNotEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this, getString(R.string.correct_login), Toast.LENGTH_LONG)
                            .show()
                    }
                    if (userLog!!.photo == "0") {
                        getLocationPermission(isPressed)
                        val intento = Intent(this, ActivitySelectAvatar::class.java)
                        startActivity(intento)
                        finish()
                    } else {
                        getLocationPermission(isPressed)
                        if (userLog!!.premium!!) {
                            val intento = Intent(this, BottomNavigationActivity::class.java)
                            startActivity(intento)
                            finish()
                        } else {
                            val intento = Intent(this, ActivityVideoView::class.java)
                            startActivity(intento)
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(this, getString(R.string.incorrect_password), Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(this, getString(R.string.username_dontexists), Toast.LENGTH_LONG)
                    .show()
            }
        }
        binding.ibFacebook.setOnClickListener {
            openAdvertiserWebsite("https://www.facebook.com/")
        }
        binding.ibGmail.setOnClickListener {
            openAdvertiserWebsite("https://www.gmail.com/")
        }
        binding.ibInstagram.setOnClickListener {
            openAdvertiserWebsite("https://www.instagram.com/")
        }
        binding.ibLinkedin.setOnClickListener {
            openAdvertiserWebsite("https://www.linkedin.com/")
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
        //   val inputStream: InputStream = resources.openRawResource(R.raw.certificadoserver) //Server PC ADRI
        val inputStream: InputStream =
            resources.openRawResource(R.raw.certificadoserverhdd) // Server PC HDD

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
                    getString(R.string.permision_acces_file),
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
                        getString(R.string.permision_acces_coarse),
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
        var locationByUser: com.example.justmeet.Models.Location? = null
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

                        runBlocking {
                            var corrutina = launch {
                                val crudApi = CrudApi()
                                locationByUser =
                                    crudApi.getOneLocationByUserFromAPI(userLog!!.idUser!!)
                            }
                            corrutina.join()

                        }
                        var userLocation: com.example.justmeet.Models.Location =
                            com.example.justmeet.Models.Location(
                                locationByUser!!.idLocation,
                                location!!.longitude,
                                location!!.latitude,
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

    private fun openAdvertiserWebsite(website: String) {
        val advertiserUri = Uri.parse(website)
        val intent = Intent(Intent.ACTION_VIEW, advertiserUri)
        startActivity(intent)
    }

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
}