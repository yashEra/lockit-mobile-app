package com.example.lockit_mobile_app

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException


class Login : AppCompatActivity() {

    private lateinit var client: OkHttpClient

    private lateinit var createAccount: Button

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText


    private  lateinit var buttonSignIn: Button

    private lateinit var loading: ProgressBar

    private val getURL: String = "https://reqres.in/api/users"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        editTextUsername = findViewById(R.id.username)
        editTextPassword = findViewById(R.id.password)

        buttonSignIn = findViewById(R.id.signin)

        createAccount = findViewById(R.id.createAccount)

        loading = findViewById(R.id.progressBar)

        createAccount.setOnClickListener{
            val intent = Intent(applicationContext,SignUp::class.java)
            startActivity(intent)
            finish()
        }

        client = OkHttpClient()

        buttonSignIn.setOnClickListener{
            loading.visibility = View.VISIBLE
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()


            if (!isNetworkAvailable()) {
                Toast.makeText(this@Login, "No internet connection", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            if (username.isEmpty()) {
                Toast.makeText(this@Login, "Username can't be empty", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this@Login, "Password can't be empty", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            signup(username, password)
        }

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Network is available
//                Toast.makeText(this@Login, "Online", Toast.LENGTH_SHORT).show()
            }

            override fun onLost(network: Network) {
                // Network is lost
                Toast.makeText(this@Login, "Offline", Toast.LENGTH_SHORT).show()

            }
        }

        val networkRequest = NetworkRequest.Builder().build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isConnected = capabilities != null

        connectivityManager.unregisterNetworkCallback(networkCallback)

        return isConnected
    }

    private fun signup(username: String, password: String) {
        val requestBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()

        val request = Request.Builder().url(getURL).post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    loading.visibility = View.GONE
                    Toast.makeText(this@Login, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    loading.visibility = View.GONE
                    try {
                        Toast.makeText(this@Login, response.body?.string(), Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext,DashBoard::class.java)
                        startActivity(intent)
                        finish()
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                }
            }
        })
    }

}