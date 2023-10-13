package com.example.myapplication.rentcarapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.User;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.AuthViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextInputEditText emailText;
    AuthViewModel authViewModel;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailText = findViewById(R.id.emailText);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void resetPassword(View view){
        String email = Objects.requireNonNull(emailText.getText()).toString();
        authViewModel.getClientUserByEmail(email).observe(this, user -> {
            if(user != null){
                getUser(user);
            }else{
                Toast.makeText(getApplicationContext(), "There is no such email. Please enter a valid email.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUser(String username){
        authViewModel.getUserByUsername(username).observe(this, user -> {
            if(user != null){
                goToResetPasswordActivity(user);
            }else{
                Toast.makeText(getApplicationContext(), "Something happened wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goToResetPasswordActivity(User user){
        String email = Objects.requireNonNull(emailText.getText()).toString();
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("User", user);
        intent.putExtra("Email", email);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}