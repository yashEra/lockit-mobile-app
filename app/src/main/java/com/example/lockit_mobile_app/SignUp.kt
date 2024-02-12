package com.example.lockit_mobile_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SignUp : AppCompatActivity()  {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        val b1: Button = findViewById(R.id.createacc)
        b1.setOnClickListener {
            val i = Intent(this@SignUp, MainActivity::class.java)
            startActivity(i)
        }

        val b2: Button = findViewById(R.id.login)
        b2.setOnClickListener {
            val i = Intent(this@SignUp, MainActivity::class.java)
            startActivity(i)
        }
    }
}
