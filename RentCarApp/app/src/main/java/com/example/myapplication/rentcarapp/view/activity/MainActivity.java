package com.example.myapplication.rentcarapp.view.activity;

import androidx.annotation.Nullable;
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
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.AuthViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    AuthViewModel authViewModel;
    TextInputEditText signInUsername, signInPassword;
    GoogleSignInClient googleSignInClient;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInUsername = findViewById(R.id.signInUsername);
        signInPassword = findViewById(R.id.signInPassword);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void signIn(View view){
        String username = Objects.requireNonNull(signInUsername.getText()).toString();
        String password = Objects.requireNonNull(signInPassword.getText()).toString();
        authViewModel.getUserByUsernameAndPassword(username, password).observe(this, user -> {
            if(user != null){
                getClientEmail();
            }else{
                Toast.makeText(getApplicationContext(), "The username and password you entered does not exist. Please check everything again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signInByGoogle(View view){
        initGoogleSignInClient();
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 1234);
    }

    private void initGoogleSignInClient(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void getGoogleAuthCredential(GoogleSignInAccount googleSignInAccount){
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential credential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogleAuthCredential(credential);
    }

    private void signInWithGoogleAuthCredential(AuthCredential credential){
        authViewModel.authenticationByGoogle(credential);
        Intent intent = new Intent(this, MainWindowActivity.class);
        startActivity(intent);
    }

    public void signUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void forgotPassword(View view){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void getClientEmail(){
        String username = Objects.requireNonNull(signInUsername.getText()).toString();
        authViewModel.getClientEmailByUsername(username).observe(this, s -> {
            if(s != null){
                authorization(s);
            }else{
                Toast.makeText(getApplicationContext(), "Something was happened. Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void authorization(String email){
        String password = Objects.requireNonNull(signInPassword.getText()).toString();
        authViewModel.signInByEmailAndPassword(email, password);
        Intent intent = new Intent(this, MainWindowActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234){
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = googleSignInAccountTask.getResult(ApiException.class);
                getGoogleAuthCredential(googleSignInAccount);
            }catch (ApiException e){
                e.printStackTrace();
            }
        }
    }
}