package com.example.lockit_mobile_app

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val b1: Button = findViewById(R.id.signin)
        b1.setOnClickListener {
            val i = Intent(this@MainActivity, Dashboard::class.java)
            startActivity(i)
        }
        val b2: Button = findViewById(R.id.createAccount)
        b2.setOnClickListener {
            val i = Intent(this@MainActivity, SignUp::class.java)
            startActivity(i)
        }
    }
}
