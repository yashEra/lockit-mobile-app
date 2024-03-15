package com.example.lockit_mobile_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PasswordEdite : AppCompatActivity() {
    private lateinit var welcomUsernameTextView: TextView
    private lateinit var cancelButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_edite)

        welcomUsernameTextView = findViewById(R.id.welcomeuser)
        cancelButton = findViewById(R.id.cancel)

        // Retrieve username from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        // Display username in TextView
        welcomUsernameTextView.text = "hi, $username"

        cancelButton.setOnClickListener{
            val intent = Intent(applicationContext,Profile::class.java)
            startActivity(intent)
            finish()
        }

    }
}