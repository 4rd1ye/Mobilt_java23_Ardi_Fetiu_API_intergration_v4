package com.gritacademy.mobilt_java23_ardi_fetiu_api_intergration_v4

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ConversionActivity : AppCompatActivity() {

    private lateinit var fromCurrencySpinner: Spinner
    private lateinit var toCurrencySpinner: Spinner
    private lateinit var amountEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversion)

        fromCurrencySpinner = findViewById(R.id.spinnerFromCurrency)
        toCurrencySpinner = findViewById(R.id.spinnerToCurrency)
        amountEditText = findViewById(R.id.editTextAmount)
        resultTextView = findViewById(R.id.textViewResult)
        val convertButton: Button = findViewById(R.id.buttonConvert)
        val showHistoryButton: Button = findViewById(R.id.buttonHistory)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("conversion_history", Context.MODE_PRIVATE)

        // Set up spinners with currency codes
        val currencies = arrayOf("USD", "SEK", "EUR", "GBP", "JPY")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromCurrencySpinner.adapter = adapter
        toCurrencySpinner.adapter = adapter

        convertButton.setOnClickListener {
            convertCurrency()
        }

        // Show History button click listener
        showHistoryButton.setOnClickListener {
            // Navigate to HistoryActivity
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun convertCurrency() {
        val fromCurrency = fromCurrencySpinner.selectedItem.toString()
        val toCurrency = toCurrencySpinner.selectedItem.toString()
        val amount = amountEditText.text.toString().toDoubleOrNull() ?: return

        val apiKey = "9c2bdf4e4c701b7d7fc74c80"
        val url = "https://api.exchangerate-api.com/v4/latest/$fromCurrency"

        Thread {
            val connection = URL(url).openConnection() as HttpURLConnection
            try {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(response)
                val rates = jsonObject.getJSONObject("rates")
                val rate = rates.getDouble(toCurrency)
                val convertedAmount = amount * rate

                // Store the conversion result in SharedPreferences
                saveConversionToHistory(fromCurrency, toCurrency, amount, convertedAmount)

                runOnUiThread {
                    resultTextView.text = String.format("Converted Amount: %.2f %s", convertedAmount, toCurrency)
                }
            } finally {
                connection.disconnect()
            }
        }.start()
    }

    private fun saveConversionToHistory(fromCurrency: String, toCurrency: String, amount: Double, convertedAmount: Double) {
        val editor = sharedPreferences.edit()

        // Retrieve existing history from SharedPreferences
        val history = sharedPreferences.getStringSet("history", mutableSetOf()) ?: mutableSetOf()

        // Add new conversion record to history
        val record = "$fromCurrency to $toCurrency: $amount -> $convertedAmount"
        history.add(record)

        // Save updated history back to SharedPreferences
        editor.putStringSet("history", history)
        editor.apply()
    }
}
