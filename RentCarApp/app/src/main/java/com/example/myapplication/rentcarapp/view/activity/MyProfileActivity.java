package com.example.myapplication.rentcarapp.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
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
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.AuthViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MyProfileActivity extends AppCompatActivity {
    AuthViewModel authViewModel;
    TextInputEditText accountFullName, accountPhone;
    ShapeableImageView updateAvatar;
    Client clientCurrent;
    Uri uri;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        accountFullName = findViewById(R.id.accountFullName);
        accountPhone = findViewById(R.id.accountPhone);
        updateAvatar = findViewById(R.id.updateAvatar);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        initData();
    }

    public void back(View view){
        Intent intent = new Intent(this, MainWindowActivity.class);
        startActivity(intent);
    }

    public void updateProfile(View view){
        String phone = Objects.requireNonNull(accountPhone.getText()).toString();
        authViewModel.isUserPhoneCorrect(phone).observe(this, aBoolean -> {
            if(aBoolean){
                updateClient();
            }else{
                Toast.makeText(getApplicationContext(), "Your phone doesn't right. Please check phone again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void changePhoto(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        startActivityForResult(intent, 1000);
    }

    private void updateClient(){
        String fullName = Objects.requireNonNull(accountFullName.getText()).toString();
        String phone = Objects.requireNonNull(accountPhone.getText()).toString();
        clientCurrent.setFullName(fullName);
        clientCurrent.setPhoneNumber(phone);
        clientCurrent.setPhoto(uri.toString());
        authViewModel.saveImageInCloudStorage(clientCurrent);
        Toast.makeText(getApplicationContext(), "Profile was updated", Toast.LENGTH_LONG).show();
    }

    private void initData(){
        authViewModel.getClient().observe(this, client -> {
            if(client != null){
                initComponents(client);
            }
        });
    }

    private void initComponents(Client client){
        clientCurrent = client;
        accountFullName.setText(client.getFullName());
        accountPhone.setText(client.getPhoneNumber());
        Picasso.get().load(client.getPhoto()).into(updateAvatar);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1000){
                if(data != null){
                    uri = data.getData();
                    updateAvatar.setImageURI(data.getData());
                    updateAvatar.setRotation(0);
                }
            }
        }
    }
}