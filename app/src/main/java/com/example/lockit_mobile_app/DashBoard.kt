package com.example.lockit_mobile_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashBoard : AppCompatActivity() {
    private lateinit var usernameTextView: TextView
    private lateinit var manageDevicePage: TextView
    private lateinit var profilePage: TextView
    private lateinit var alertsPage: TextView
    private lateinit var helpPage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        // Initialize views
        usernameTextView = findViewById(R.id.username)
        manageDevicePage = findViewById(R.id.manageDevice)
        profilePage = findViewById(R.id.useraccount)
        alertsPage = findViewById(R.id.alert)
        helpPage = findViewById(R.id.help)



        // Retrieve username from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        // Display username in TextView
        usernameTextView.text = "hi, $username"

        manageDevicePage.setOnClickListener{
            val intent = Intent(applicationContext,AddDevice::class.java)
            startActivity(intent)
            finish()
        }

        profilePage.setOnClickListener{
            val intent = Intent(applicationContext,Profile::class.java)
            startActivity(intent)
            finish()
        }


    }
}
