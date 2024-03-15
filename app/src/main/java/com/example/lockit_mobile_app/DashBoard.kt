package com.example.lockit_mobile_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashBoard : AppCompatActivity() {
    private lateinit var usernameTextView: TextView
    private lateinit var manageDevice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        // Initialize views
        usernameTextView = findViewById(R.id.username)
        manageDevice = findViewById(R.id.manageDevice)


        // Retrieve username from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        // Display username in TextView
        usernameTextView.text = "hi, $username"

        manageDevice.setOnClickListener{
            val intent = Intent(applicationContext,AddDevice::class.java)
            startActivity(intent)
            finish()
        }


    }
}
