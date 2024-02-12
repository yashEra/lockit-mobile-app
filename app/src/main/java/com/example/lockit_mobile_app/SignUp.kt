package com.example.lockit_mobile_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class SignUp : AppCompatActivity()  {

    private lateinit var client: OkHttpClient

    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextRetypePassword: EditText

    private lateinit var buttonSignup: Button

    private lateinit var loading: ProgressBar

    private lateinit var login: TextView

//    private val postURL: String = "http://10.0.2.2:5001/lockit-332b1/us-central1/app/register"
    private val getURL: String = "https://reqres.in/api/users"


    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        client = OkHttpClient()

        editTextUsername = findViewById(R.id.username)
        editTextEmail = findViewById(R.id.email)
        editTextPhoneNumber = findViewById(R.id.phonenumber)
        editTextPassword = findViewById(R.id.password)
        editTextRetypePassword = findViewById(R.id.retypepassword)

        buttonSignup = findViewById(R.id.signup)

        loading = findViewById(R.id.progressBar)

        login = findViewById(R.id.login)

        login.setOnClickListener{
            val intent = Intent(applicationContext,Login::class.java)
            startActivity(intent)
            finish()
        }

        buttonSignup.setOnClickListener {
            loading.visibility = View.VISIBLE
            val username = editTextUsername.text.toString()
            val email = editTextEmail.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()
            val password = editTextPassword.text.toString()
            val retypePassword = editTextRetypePassword.text.toString()

            if (!isNetworkAvailable()) {
                Toast.makeText(this@SignUp, "No internet connection", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            if (username.isEmpty()) {
                Toast.makeText(this@SignUp, "Username can't be empty", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            if (email.isEmpty() || !isValidEmail(email)) {
                Toast.makeText(this@SignUp, "Enter a valid email address", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            if (phoneNumber.isEmpty()) {
                Toast.makeText(this@SignUp, "Phone Number can't be empty", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this@SignUp, "Password can't be empty", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            if (retypePassword.isEmpty()) {
                Toast.makeText(this@SignUp, "Retype password can't be empty", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            if (password != retypePassword) {
                Toast.makeText(this@SignUp, "Passwords are not matching", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            signup(username, email, phoneNumber, password)
        }

    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Network is available
//                Toast.makeText(this@SignUp, "Online", Toast.LENGTH_SHORT).show()
            }

            override fun onLost(network: Network) {
                // Network is lost
                Toast.makeText(this@SignUp, "Offline", Toast.LENGTH_SHORT).show()
            }
        }

        val networkRequest = NetworkRequest.Builder().build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isConnected = capabilities != null

        connectivityManager.unregisterNetworkCallback(networkCallback)

        return isConnected
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        return email.matches(emailRegex.toRegex())
    }

    private fun signup(username: String, email: String, phoneNumber: String, password: String) {
        val requestBody = FormBody.Builder()
            .add("username", username)
            .add("email", email)
            .add("phoneNumber", phoneNumber)
            .add("password", password)
            .build()

        val request = Request.Builder().url(getURL).post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    loading.visibility = View.GONE
                    Toast.makeText(this@SignUp, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    loading.visibility = View.GONE
                    try {
                        Toast.makeText(this@SignUp, response.body?.string(), Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                }
            }
        })
    }

}
