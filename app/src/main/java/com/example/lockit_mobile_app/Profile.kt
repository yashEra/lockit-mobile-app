package com.example.lockit_mobile_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class Profile : AppCompatActivity() {
    private lateinit var welcomUsernameTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var changePasswordTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var backButton: Button
    private lateinit var singoutButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        welcomUsernameTextView = findViewById(R.id.welcomeuser)
        usernameTextView = findViewById(R.id.username)
        emailTextView = findViewById(R.id.email)
        phoneTextView = findViewById(R.id.phonenumber)
        backButton = findViewById(R.id.back)
        changePasswordTextView = findViewById(R.id.password)
        editProfileButton = findViewById(R.id.edidProfile)
        singoutButton = findViewById(R.id.signout)


        // Retrieve username from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        val email = sharedPreferences.getString("email", "")
        val phone = sharedPreferences.getString("phoneNumber", "")

        // Display username in TextView
        welcomUsernameTextView.text = "hi, $username"
        usernameTextView.text = username
        emailTextView.text = email
        phoneTextView.text = phone

        backButton.setOnClickListener{
            val intent = Intent(applicationContext,DashBoard::class.java)
            startActivity(intent)
            finish()
        }

        changePasswordTextView.setOnClickListener{
            val intent = Intent(applicationContext,PasswordEdite::class.java)
            startActivity(intent)
            finish()
        }

        editProfileButton.setOnClickListener{
            val intent = Intent(applicationContext,EditProfile::class.java)
            startActivity(intent)
            finish()
        }

        singoutButton.setOnClickListener {
            // Clear SharedPreferences
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            Toast.makeText(this@Profile, "Logout successfully", Toast.LENGTH_SHORT).show()

            // Navigate to the login screen
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }


    }
}