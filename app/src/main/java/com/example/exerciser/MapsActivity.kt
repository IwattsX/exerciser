package com.example.exerciser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.exerciser.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var longitude : Double = 0.0
    private var latitude : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    getUpdatedLocations()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    getUpdatedLocations()
                }
                else -> {
                    // Inform the user that location permissions are necessary
                    Toast.makeText(this, "You don't have locations enabled", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            // TODO: get the results of this and then call getLastKnownLocation
        } else {
            // getLastKnownLocation()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getUpdatedLocations()
        }

        val bottomNavigationView = findViewById <BottomNavigationView> (R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_navigation_map -> {
                    // Handle home tab click
                    mapScreen()
                    true
                }
                R.id.bottom_navigation_inventory -> {
                    // Handle search tab click
                    openInventoryScreen()
                    true
                }
                R.id.bottom_navigation_step_counter -> {
                    // Handle profile tab click
                    stepCounterScreen()
                    true
                }

                // ... other tab handlers
                else -> {
                    // Handle other cases
                    true
                }
            }
        }

    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.v("Location callback called", "Location updated now is location not null")

            for (location in locationResult.locations) {

                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude

                    val pos = LatLng(latitude, longitude)
                    mMap.addMarker(MarkerOptions().position(pos).title("You"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 25f))
                }
            }
        }
    }
    private fun getUpdatedLocations(){
        Log.v("Update location", "Location getting updated")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper())

    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 5 * 1000)
            .build()
    }

    private fun getLastKnownLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            requestPermissions(
//                arrayOf(
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ), 0 // REQUEST_CODE
//            )
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    val pos = LatLng(latitude, longitude)
                    mMap.addMarker(MarkerOptions().position(pos).title("You"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
                } else {
                    Toast.makeText(this, "You don't have locations enabled, location not available", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun mapScreen() {
        val mapsIntent = Intent(this, MapsActivity::class.java)
        startActivity(mapsIntent)
    }

    private fun stepCounterScreen(){
        val stepCounterIntent = Intent(this, StepCounterActivity::class.java)
        startActivity(stepCounterIntent)
    }

    private fun openInventoryScreen() {
        val inventoryIntent = Intent(this, InventoryActivity::class.java)
        startActivity(inventoryIntent)
    }
}