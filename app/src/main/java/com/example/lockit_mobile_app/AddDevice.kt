package com.example.lockit_mobile_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class AddDevice : AppCompatActivity() {
    private lateinit var backButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_device)

        backButton = findViewById(R.id.back)

        backButton.setOnClickListener{
            val intent = Intent(applicationContext,DashBoard::class.java)
            startActivity(intent)
            finish()
        }

    }
}