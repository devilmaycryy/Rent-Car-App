package com.example.myapplication.rentcarapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.Rent;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.CarViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class OrderCarActivity extends AppCompatActivity {
    HashMap<String, Integer> dataAboutCar;
    Spinner stationIssuingSpinner, stationReturnSpinner;

    TextView dateIssuing, dateReturn, totalAmountText, sum;
    CarViewModel carViewModel;
    BroadcastReceiver broadcastReceiver;
    Date current;
    String token = "";
    String tokenClient = "";
    int price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_car);
        stationIssuingSpinner = findViewById(R.id.stationIssuingSpinner);
        stationReturnSpinner = findViewById(R.id.stationReturnSpinner);
        dateIssuing = findViewById(R.id.dateAndTimeIssuing);
        dateReturn = findViewById(R.id.dateAndTimeReturn);
        totalAmountText = findViewById(R.id.totalAmount);
        sum = findViewById(R.id.sum);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        dataAboutCar = (HashMap<String, Integer>) getIntent().getSerializableExtra("DataAboutCar");
        broadcastReceiver = new InternetReceiver();
        current = new Date();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        initData();
        giveDataFromHashMap();
        changeTextView();
    }

    private void initData(){
        carViewModel.getStations().observe(this, strings -> {
            String[] stations = strings.toArray(new String[0]);
            initSpinner(stations);
        });
        carViewModel.getClient().observe(this, client -> tokenClient = client.getToken());
    }

    private void initSpinner(String[] stations){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, stations);
        stationIssuingSpinner.setAdapter(adapter);
        stationReturnSpinner.setAdapter(adapter);
    }

    public void rentCar(View view){
        Random random = new Random();
        String stationIssuing = stationIssuingSpinner.getSelectedItem().toString();
        String stationReturn = stationReturnSpinner.getSelectedItem().toString();
        String startDate = dateIssuing.getText().toString();
        String endDate = dateReturn.getText().toString();
        String id = String.valueOf(random.nextInt(10000));
        Rent rent = new Rent(id, tokenClient, token, stationIssuing, stationReturn, startDate, startDate, endDate, price, price);
        rent.setFines(0);
        rent.setStatus("No return");
        carViewModel.createRent(rent);
        Intent intent = new Intent(this, PaymentMethodActivity.class);
        startActivity(intent);
    }

    public void pickDateIssuingRent(View view){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDate.now();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> dateIssuing.setText(day + "." + (month + 1) + "." + year), date.getYear(), date.getMonth().getValue() - 1, date.getDayOfMonth());
            datePickerDialog.getDatePicker().setMinDate(current.getTime());
            datePickerDialog.show();
        }
    }

    public void pickDateReturnRent(View view){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = LocalDate.now();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> dateReturn.setText(day + "." + (month + 1) + "." + year), date.getYear(), date.getMonth().getValue() - 1, date.getDayOfMonth());
            datePickerDialog.getDatePicker().setMinDate(current.getTime());
            datePickerDialog.show();
        }
    }

    private void calculateDifferenceBetweenTwoDates(String startDate, String endDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            Date beginDate = simpleDateFormat.parse(startDate);
            Date finalDate = simpleDateFormat.parse(endDate);
            long difference_In_Time = finalDate.getTime() - beginDate.getTime();
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            price *= difference_In_Days;
            sum.setText(price + " ₴");
            totalAmountText.setText(price + " ₴");
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void changeTextView(){
        dateReturn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String startDate = dateIssuing.getText().toString();
                String endDate = dateReturn.getText().toString();
                if (startDate.length() != 0 && endDate.length() != 0) {
                    calculateDifferenceBetweenTwoDates(startDate, endDate);
                }
            }
        });
    }

    private void giveDataFromHashMap(){
        for(Map.Entry<String, Integer> entry: dataAboutCar.entrySet()){
            token = entry.getKey();
            price = entry.getValue();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}