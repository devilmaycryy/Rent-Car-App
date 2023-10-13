package com.example.myapplication.rentcarapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.Rent;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.CarViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RentDetailActivity extends AppCompatActivity {
    Rent rent;
    MaterialButton returnCar, cancelRent;
    TextView rentIDCar, issuingStationRent, returnStationRent,
            dateIssuingRent, dateReturnRent, finesRent,
            sumRent, amountRent, statusRent;
    CarViewModel carViewModel;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_detail);
        rent = (Rent) getIntent().getSerializableExtra("Rent");
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        initOther();
        initStation();
        initDate();
        initPrice();
        checkDateToReturnCar();
        checkDateToIssuingCar();
    }

    private void initStation(){
        issuingStationRent = findViewById(R.id.issuingStationRent);
        returnStationRent = findViewById(R.id.returnStationRent);
        issuingStationRent.setText(rent.getStationIssuing());
        returnStationRent.setText(rent.getStationReturn());
    }

    private void initDate(){
        dateIssuingRent = findViewById(R.id.dateIssuingRent);
        dateReturnRent = findViewById(R.id.dateReturnRent);
        dateIssuingRent.setText(rent.getIssuingDate());
        dateReturnRent.setText(rent.getReturnDate());
    }

    private void initPrice(){
        finesRent = findViewById(R.id.finesRent);
        sumRent = findViewById(R.id.sumRent);
        amountRent = findViewById(R.id.amountRent);
        finesRent.setText(rent.getFines() + " ₴");
        sumRent.setText(rent.getSum() + " ₴");
        amountRent.setText(rent.getTotalAmount() + " ₴");
    }

    private void initOther(){
        rentIDCar = findViewById(R.id.rentIDCar);
        statusRent = findViewById(R.id.statusRent);
        returnCar = findViewById(R.id.returnCar);
        cancelRent = findViewById(R.id.cancelRent);
        rentIDCar.setText(rent.getCar());
        statusRent.setText(rent.getStatus());
        returnCar.setEnabled(false);
    }


    private void checkDateToReturnCar(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            Date currentDate = new Date();
            Date returnDate = simpleDateFormat.parse(rent.getReturnDate());
            long difference_In_Time = returnDate.getTime() - currentDate.getTime();
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            isDateToReturnCar(difference_In_Days);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void checkDateToIssuingCar(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            Date currentDate = new Date();
            Date issuingDate = simpleDateFormat.parse(rent.getIssuingDate());
            long difference_In_Time = currentDate.getTime() - issuingDate.getTime();
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            isCanCancelRent(difference_In_Days);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void isCanCancelRent(long difference_In_Days){
        if(difference_In_Days >= 2){
            cancelRent.setEnabled(false);
            cancelRent.setVisibility(View.GONE);
        }
    }

    public void onCancelRent(View view){
        carViewModel.deleteRent(rent.getId());
        Toast.makeText(getApplicationContext(), "Rent " + rent.getId() + " was canceled", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainWindowActivity.class);
        startActivity(intent);
    }

    private void isDateToReturnCar(long difference_In_Days){
        if(difference_In_Days >= 0){
            returnStationRent.setEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}