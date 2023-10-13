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

public class ResetPasswordActivity extends AppCompatActivity {
    TextInputEditText resetNewPassword, resetConfirmPassword;
    User user;
    String email;
    AuthViewModel authViewModel;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetNewPassword = findViewById(R.id.resetNewPassword);
        resetConfirmPassword = findViewById(R.id.resetConfirmPassword);
        user = (User) getIntent().getSerializableExtra("User");
        email = getIntent().getStringExtra("Email");
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void confirmPassword(View view){
        String newPassword = Objects.requireNonNull(resetNewPassword.getText()).toString();
        String confirmNewPassword = Objects.requireNonNull(resetConfirmPassword.getText()).toString();
        authViewModel.isNewPasswordWriteCorrect(newPassword, confirmNewPassword).observe(this, aBoolean -> {
            if(aBoolean){
                updatePassword();
            }else{
                Toast.makeText(getApplicationContext(), "An error has occurred. The password may be small or the confirmation of the new password does not match. Please check again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updatePassword(){
        String newPassword = Objects.requireNonNull(resetNewPassword.getText()).toString();
        authViewModel.updateUsersPassword(user, email, newPassword);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}