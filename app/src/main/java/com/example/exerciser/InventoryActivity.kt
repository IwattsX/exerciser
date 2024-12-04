package com.example.exerciser

import android.content.Intent
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class InventoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        val bottomNavigationView = findViewById <BottomNavigationView> (R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_navigation_map -> {
                    // Handle home tab click
                    mapScreen()
                    true
                }

                R.id.bottom_navigation_inventory -> {
                    // Handle inventory tab click
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

        val tableLayout = findViewById<TableLayout>(R.id.inventoryBag)

        for (i in 1..5) { // Add 5 rows
            val tableRow = TableRow(this)
            for (j in 1..5) { // Add 5 cells per row
                val textView = TextView(this)
                textView.text = "Cell $i,$j"
                textView.background = getDrawable(R.drawable.table_border) // Apply border
                tableRow.addView(textView)
            }
            tableRow.background = getDrawable(R.drawable.table_border) // Apply border to row
            tableLayout.addView(tableRow)
        }

        tableLayout.background = getDrawable(R.drawable.table_border) // Apply border to TableLayout

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