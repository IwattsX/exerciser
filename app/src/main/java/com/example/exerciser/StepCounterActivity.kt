package com.example.exerciser

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast

class StepCounterActivity : AppCompatActivity(), SensorEventListener {
    private var running = false

    private var sensorManager: SensorManager? = null

    private var stepCount = 0

    private var totalSteps = 0
    private var previousMagnitude = 0f

    private lateinit var stepTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)

        stepTextView = findViewById(R.id.stepCount)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Load the previous total steps if available (e.g., from SharedPreferences)
        loadPreviousTotalSteps()
    }

    override fun onResume() {
        super.onResume()
        running = true

        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (stepSensor == null) {
            Log.e("StepCounter", "Step counter sensor not available")
            Toast.makeText(this, "No step counter sensor available", Toast.LENGTH_SHORT).show()
        } else {
            val isRegistered = sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL) ?: false
            if (!isRegistered) {
                Log.e("StepCounter", "Failed to register step sensor listener")
                Toast.makeText(this, "Could not register step counter sensor", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Step counter sensor registered", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            if (event != null) {
                // Get the total steps from the sensor
                val x_axis = event.values[0]
                val y_axis = event.values[1]
                val z_axis = event.values[2]

                // Calculate the magnitude of the acceleration vector
                val magnitude = Math.sqrt((x_axis * x_axis + y_axis * y_axis + z_axis * z_axis).toDouble()).toFloat()
                val magnitudeDelta = magnitude - previousMagnitude

                if (magnitudeDelta > 10) {
                    stepCount++

                }

                if(stepCount >= 30){
                    stepCount -= 30
                    totalSteps++
                    stepTextView.text = totalSteps.toString()
                }


            } else {
               Toast.makeText(this, "Event for sensor is null, can't update steps", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ... onAccuracyChanged() ...

    private fun loadPreviousTotalSteps() {
        val sharedPreferences = getSharedPreferences("stepCounterPrefs", Context.MODE_PRIVATE)
        val previousTotalSteps = sharedPreferences.getFloat("previousTotalSteps", 0f)
        stepCount = previousTotalSteps.toInt() // Initialize cumulative steps
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No action needed for accuracy changes
    }


    private fun savePreviousTotalSteps() {
        val sharedPreferences = getSharedPreferences("stepCounterPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("previousTotalSteps", stepCount.toFloat())
        editor.apply()
    }

    override fun onStop() {
        super.onStop()
        // Save the current cumulative steps when the app stops
        savePreviousTotalSteps()
    }
}

