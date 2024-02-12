package com.example.lockit_mobile_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Signup extends AppCompatActivity {

    OkHttpClient client;
    EditText editTextUsername,editTextEmail,editTextPhoneNumber,editTextPassword,editTextRetypePassword;
    Button buttonSignup;
    ProgressBar loding;
    TextView login;
    String getURL = "";
    String postUrl = "http://10.0.2.2:5001/lockit-332b1/us-central1/app/register";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        client = new OkHttpClient();
        editTextUsername = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.email);
        editTextPhoneNumber = findViewById(R.id.phonenumber);
        editTextPassword = findViewById(R.id.password);
        editTextRetypePassword = findViewById(R.id.retypepassword);
        buttonSignup = findViewById(R.id.signup);
        loding = findViewById(R.id.progressBar);
        login = findViewById(R.id.loginText);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loding.setVisibility(View.VISIBLE);
                String username,email,phonNumber,password,retypePassword;
                username = String.valueOf(editTextUsername.getText());
                email = String.valueOf(editTextEmail.getText());
                phonNumber = String.valueOf(editTextPhoneNumber.getText());
                password = String.valueOf(editTextPassword.getText());
                retypePassword = String.valueOf(editTextRetypePassword.getText());

                if (!isNetworkAvailable()) {
                    Toast.makeText(Signup.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(username)){
                    Toast.makeText(Signup.this, "Username can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
                    Toast.makeText(Signup.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phonNumber)){
                    Toast.makeText(Signup.this, "Phone Number can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(retypePassword)){
                    Toast.makeText(Signup.this, "Retype password can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.equals(password,retypePassword)){
                    Toast.makeText(Signup.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    return;
                }

                signup(username,email,phonNumber,password);
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public void signup(String username,String email,String phonNumber,String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("username",username)
                .add("email",email)
                .add("phoneNumber",phonNumber)
                .add("password",password)
                .build();
        Request request = new Request.Builder().url(postUrl).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                loding.setVisibility(View.GONE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Signup.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                loding.setVisibility(View.GONE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(Signup.this, response.body().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

        });
    }
}