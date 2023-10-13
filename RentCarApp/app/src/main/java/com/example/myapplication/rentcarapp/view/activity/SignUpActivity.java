package com.example.myapplication.rentcarapp.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.Client;
import com.example.myapplication.rentcarapp.model.firestore.models.User;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.AuthViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    ShapeableImageView icon;
    Uri uri;
    AuthViewModel authViewModel;
    TextInputEditText newUsername, newFullName, newEmail, userPassword, newPhone;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        icon = findViewById(R.id.avatar);
        newUsername = findViewById(R.id.newUsername);
        newFullName = findViewById(R.id.newFullName);
        newEmail = findViewById(R.id.newEmail);
        userPassword = findViewById(R.id.userPassword);
        newPhone = findViewById(R.id.newPhone);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    public void pickUpImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1000){
                if(data != null){
                    uri = data.getData();
                    icon.setImageURI(data.getData());
                }
            }
        }
    }

    public void createAccount(View view){
        String email = Objects.requireNonNull(newEmail.getText()).toString();
        String password = Objects.requireNonNull(userPassword.getText()).toString();
        authViewModel.isEmailAndPasswordWriteCorrect(email, password).observe(this, aBoolean -> {
            if(aBoolean && uri != null){
                checkPhone();
            }else{
                Toast.makeText(getApplicationContext(), "Your email and password don't right or you might not chose image. Please check email and password or choose image again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkPhone(){
        String phone = Objects.requireNonNull(newPhone.getText()).toString();
        authViewModel.isUserPhoneCorrect(phone).observe(this, aBoolean -> {
            if(aBoolean){
                checkUsername();
            }else{
                Toast.makeText(getApplicationContext(), "Your phone doesn't right. Please check phone again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkUsername(){
        String username = Objects.requireNonNull(newUsername.getText()).toString();
        String password = Objects.requireNonNull(userPassword.getText()).toString();
        authViewModel.getUserByUsername(username).observe(this, user -> {
            if(user == null){
                User newUser = new User(username, password);
                authViewModel.createUser(newUser);
                createClient();
            }else{
                Toast.makeText(getApplicationContext(), "Sorry, but such username exists. Enter another.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createClient(){
        String username = Objects.requireNonNull(newUsername.getText()).toString();
        String fullName = Objects.requireNonNull(newFullName.getText()).toString();
        String email = Objects.requireNonNull(newEmail.getText()).toString();
        String password = Objects.requireNonNull(userPassword.getText()).toString();
        String phone = Objects.requireNonNull(newPhone.getText()).toString();
        Client newClient = new Client(fullName, phone, email, username, uri.toString());
        authViewModel.signUpWithEmailAndPassword(newClient, password);
        goToMainActivity();
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}