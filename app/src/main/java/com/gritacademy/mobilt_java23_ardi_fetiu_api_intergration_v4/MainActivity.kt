package com.gritacademy.mobilt_java23_ardi_fetiu_api_intergration_v4

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button to go to ConversionActivity
        val convertCurrencyButton: Button = findViewById(R.id.buttonConvert)
        convertCurrencyButton.setOnClickListener {
            val intent = Intent(this, ConversionActivity::class.java)
            startActivity(intent)
        }

        // Button to go to HistoryActivity
        val historyButton: Button = findViewById(R.id.buttonHistory)
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        // Button to go to SettingsActivity
        val settingsButton: Button = findViewById(R.id.buttonSetting)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}