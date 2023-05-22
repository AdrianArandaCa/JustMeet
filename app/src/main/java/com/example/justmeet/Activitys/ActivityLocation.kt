package com.example.justmeet.Activitys

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Models.isDebug
import com.example.justmeet.Models.userLog
import com.example.justmeet.R
import com.example.justmeet.databinding.ActivityLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ActivityLocation : AppCompatActivity(), OnMapReadyCallback, CoroutineScope,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    private lateinit var binding: ActivityLocationBinding
    private lateinit var map: GoogleMap
    var job = Job()
    val urlapi = "https://api.openrouteservice.org/"
    private var permisosGarantits: Boolean = false
    val locationRequestCode = 0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var allLocationsList: ArrayList<com.example.justmeet.Models.Location>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        demanarPermisos()
        putFullScreen()
        createFragment()
        binding.btnBackMap.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.setOnMyLocationClickListener(this)
        map.setOnMyLocationButtonClickListener(this)
        comprovarPermisos()
        createMarker()
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onMyLocationClick(ubicacioActual: Location) {
        val ubi = LatLng(ubicacioActual.latitude, ubicacioActual.longitude)
        map.addMarker(MarkerOptions().position(ubi).title(getString(R.string.current_position)))
    }

    private fun createMarker() {
        var locationByUser: com.example.justmeet.Models.Location? = null
        runBlocking {
            var corrutina = launch {
                val crudApi = CrudApi()
                allLocationsList = crudApi.getAllLocationsFromAPI()!!
            }
            corrutina.join()
        }
        for (i in 0..allLocationsList.size - 1) {
            if (allLocationsList[i].latitud != 0.0 && allLocationsList[i].longitud != 0.0) {
                val coordinates =
                    LatLng(allLocationsList[i].latitud!!, allLocationsList[i].longitud!!)
                val marker =
                    MarkerOptions().position(coordinates).title(getString(R.string.justmeet_user))
                map.addMarker(marker)
            }
        }

        runBlocking {
            var corrutina = launch {
                val crudApi = CrudApi()
                locationByUser = crudApi.getOneLocationByUserFromAPI(userLog!!.idUser!!)
            }
            corrutina.join()
        }
        val latLongUser = LatLng(locationByUser!!.latitud!!, locationByUser!!.longitud!!)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(latLongUser, 14f), 5000, null
        )
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

    fun comprovarPermisos() {
        if (
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            permisosGarantits = true
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->

//                ubicacioActualLatitud = location!!.latitude
//                ubicacioActualLongitud = location!!.longitude
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
                permisosGarantits = true
                comprovarPermisos()
            } else
                permisosGarantits = false
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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}