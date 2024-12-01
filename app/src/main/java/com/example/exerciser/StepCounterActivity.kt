package com.example.exerciser

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class StepCounterActivity : AppCompatActivity(), SensorEventListener {

    // SensorManager for managing the step counter sensor
    private var sensorManager: SensorManager? = null

    // Tracks the total number of steps
    private var totalSteps = 0f

    // TextView to display the step count
    private lateinit var stepTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)

        // Initialize the TextView
        stepTextView = findViewById(R.id.stepCount)

        // Initialize SensorManager
        stepTextView.text = "$totalSteps"
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()

        // Get the step counter sensor
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            // Display a message if the sensor is not available
            Toast.makeText(this, "No step counter sensor available on this device", Toast.LENGTH_SHORT).show()
        } else {
            // Register the sensor listener
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregister the sensor listener when the activity is not visible
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            // Update the step count
            totalSteps = event.values[0]
            stepTextView.text = totalSteps.toInt().toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No action needed for accuracy changes
    }
}
