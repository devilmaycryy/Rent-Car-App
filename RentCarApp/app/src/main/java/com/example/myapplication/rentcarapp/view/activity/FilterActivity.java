package com.example.myapplication.rentcarapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.Car;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.CarViewModel;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FilterActivity extends AppCompatActivity {
    RangeSlider priceSlider;
    RadioButton checkChilderChair, checkAutomaton, checkMechanic, checkGasoline, checkDiesel;
    CarViewModel carViewModel;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        priceSlider = findViewById(R.id.priceSlider);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        initCheckBox();
    }

    private void initCheckBox(){
        checkChilderChair = findViewById(R.id.checkChilderChair);
        checkAutomaton = findViewById(R.id.checkAutomaton);
        checkMechanic = findViewById(R.id.checkMechanic);
        checkGasoline = findViewById(R.id.checkGasoline);
        checkDiesel = findViewById(R.id.checkDiesel);
    }

    public void apply(View view){
        getCheckTransmission();
    }

    private void getCheckTransmission(){
        carViewModel.choiceTransmission(checkAutomaton.isChecked(), checkMechanic.isChecked()).observe(this, this::getCheckTypeOfFuel);
    }

    private void getCheckTypeOfFuel(String transmission){
        carViewModel.choiceTypeOfFuel(checkGasoline.isChecked(), checkDiesel.isChecked()).observe(this, s -> confirmChoice(transmission, s));
    }

    private void confirmChoice(String transmission, String typeOfFuel){
        boolean childrenChair = checkChilderChair.isChecked();
        List<Float> values = priceSlider.getValues();
        float minPrice = values.get(0);
        float maxPrice = values.get(1);
        getCarsByFilter(childrenChair, transmission, typeOfFuel, (int) minPrice, (int) maxPrice);
    }

    private void getCarsByFilter(boolean childrenChair, String transmission, String typeOfFuel, int minPrice, int maxPrice){
        carViewModel.confirmChoice(childrenChair, transmission, typeOfFuel, minPrice, maxPrice).observe(this, this::sendChoicesClient);
    }

    private void sendChoicesClient(List<Car> cars){
        Intent intent = new Intent(this, MainWindowActivity.class);
        intent.putExtra("FilterCar", (Serializable) cars);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}