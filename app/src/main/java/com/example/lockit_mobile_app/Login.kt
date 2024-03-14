package com.example.lockit_mobile_app

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import org.json.JSONException
import org.json.JSONObject


class Login : AppCompatActivity() {

    private lateinit var client: OkHttpClient

    private lateinit var createAccount: Button

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText


    private  lateinit var buttonSignIn: Button

    private lateinit var loading: ProgressBar

//    private val getURL: String = "https://reqres.in/api/users"

    private val getURL: String = "http://10.0.2.2:5001/lockit-332b1/us-central1/app/login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Check if the user is already logged in
        if (isLoggedIn()) {
            // If the user is already logged in, start the Dashboard activity
            startDashboardActivity()
            return
        }

        editTextEmail = findViewById(R.id.email)
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
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()


            if (!isNetworkAvailable()) {
                Toast.makeText(this@Login, "No internet connection", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            if (email.isEmpty() || !Utils.isValidEmail(email)) {
                Toast.makeText(this@Login, "Enter a valid email address", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this@Login, "Password can't be empty", Toast.LENGTH_SHORT).show()

                loading.visibility = View.GONE

                return@setOnClickListener
            }

            signIn(email, password)
        }

    }

    private fun isLoggedIn(): Boolean {
        // Check if user details are stored in SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)
        return !username.isNullOrEmpty()
    }

    private fun startDashboardActivity() {
        val intent = Intent(this, DashBoard::class.java)
        startActivity(intent)
        finish() // Finish the LoginActivity so the user cannot navigate back to it using the back button
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

    private fun signIn(email: String, password: String) {
        val requestBody = FormBody.Builder()
            .add("email", email)
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
                        val jsonResponse = response.body?.string()
                        Log.d("Response", jsonResponse ?: "Empty response")

                        // Convert JSON response string to JSONObject
                        val jsonObject = JSONObject(jsonResponse)

                        // Check if the response contains "status": true
                        if (jsonObject.getBoolean("status")) {
                            // If status is true, parse user details from response
                            val userObject = jsonObject.getJSONObject("user")
                            val userId = userObject.getLong("id")
                            val username = userObject.getString("username")
                            val email = userObject.getString("email")
                            val phoneNumber = userObject.getString("phoneNumber")

                            // Store user details in SharedPreferences
                            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putLong("userId", userId)
                            editor.putString("username", username)
                            editor.putString("email", email)
                            editor.putString("phoneNumber", phoneNumber)
                            editor.apply()

                            // Start Dashboard activity
                            val intent = Intent(applicationContext, DashBoard::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If status is false, display an error message
                            Toast.makeText(this@Login, "Login failed: ${jsonObject.getString("message")}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        // Handle JSON parsing error
                        Log.e("JSONException", "Error parsing JSON response", e)
                        Toast.makeText(this@Login, "Error parsing JSON response", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        // Handle IO error
                        Log.e("IOException", "Error reading response body", e)
                        Toast.makeText(this@Login, "Error reading response body", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}