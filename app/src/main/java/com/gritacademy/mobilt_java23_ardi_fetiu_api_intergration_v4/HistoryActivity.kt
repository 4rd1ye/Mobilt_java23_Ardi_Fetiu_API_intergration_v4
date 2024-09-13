package com.gritacademy.mobilt_java23_ardi_fetiu_api_intergration_v4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        listView = findViewById(R.id.listViewHistory)

        // Retrieve the stored history from SharedPreferences
        val sharedPreferences = getSharedPreferences("conversion_history", Context.MODE_PRIVATE)
        val history = sharedPreferences.getStringSet("history", mutableSetOf())?.toList() ?: listOf()

        // Prepare data for ListView
        val data = history.map { record ->
            val parts = record.split(":")
            val details = parts[1].split("->")
            mapOf("fromTo" to parts[0], "amount" to details[0], "converted" to details[1])
        }

        val fromKeys = arrayOf("fromTo", "amount", "converted")
        val adapter = SimpleAdapter(
            this,
            data,
            R.layout.list_item_history,
            fromKeys,
            intArrayOf(R.id.textViewFrom, R.id.textViewAmount, R.id.textViewConverted)
        )

        listView.adapter = adapter

        // Button to navigate to ConversionActivity
        val convertCurrencyButton: Button = findViewById(R.id.buttonConvert)
        convertCurrencyButton.setOnClickListener {
            val intent = Intent(this, ConversionActivity::class.java)
            startActivity(intent)
        }
    }
}
