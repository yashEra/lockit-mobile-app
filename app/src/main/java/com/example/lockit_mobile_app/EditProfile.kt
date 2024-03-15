package com.example.lockit_mobile_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class EditProfile : AppCompatActivity() {
    private lateinit var welcomUsernameTextView: TextView
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var cancelButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)

        welcomUsernameTextView = findViewById(R.id.welcomeuser)
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        phoneEditText = findViewById(R.id.phonenumber)
        cancelButton = findViewById(R.id.cancel)

        // Retrieve username from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        val email = sharedPreferences.getString("email", "")
        val phone = sharedPreferences.getString("phoneNumber", "")

        // Display username in TextView
        welcomUsernameTextView.text = "hi, $username"

        // Set text for EditText fields using setText() method
        usernameEditText.setText(username)
        emailEditText.setText(email)
        phoneEditText.setText(phone)

        cancelButton.setOnClickListener{
            val intent = Intent(applicationContext,Profile::class.java)
            startActivity(intent)
            finish()
        }


    }
}