package com.example.exerciser

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(){
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mapScreen()

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

    private fun mapScreen() {
        val mapsIntent = Intent(this, MapsActivity::class.java)
        startActivity(mapsIntent)
    }

    private fun stepCounterScreen(){
        val stepCounterIntent = Intent(this, StepCounterActivity::class.java)
        startActivity(stepCounterIntent)
    }


}
