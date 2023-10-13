package com.example.myapplication.rentcarapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.DriverLicence;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.CarViewModel;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class AddDriverLicenceActivity extends AppCompatActivity {
    HashMap<String, Integer> dataAboutCar;
    EditText driverLicenceNumber;
    TextView dateIssuingDriverLicence, dateValidUntilDriverLicence;
    CarViewModel carViewModel;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver_licence);
        driverLicenceNumber = findViewById(R.id.driverLicenceNumber);
        dateIssuingDriverLicence = findViewById(R.id.dateIssuingDriverLicence);
        dateValidUntilDriverLicence = findViewById(R.id.dateValidUntilDriverLicence);
        dataAboutCar = (HashMap<String, Integer>) getIntent().getSerializableExtra("AddDriverLicence");
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void addDriverLicence(View view){
        String driverLicence = driverLicenceNumber.getText().toString();
        carViewModel.isDriverLicenceNumberWriteCorrect(driverLicence).observe(this, aBoolean -> {
            if(aBoolean){
                checkClientCreditCard();
            }else{
                Toast.makeText(getApplicationContext(), "You entered incorrectly, driver license number must be 9 characters. Please check again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkClientCreditCard(){
        String driverLicenceNum = driverLicenceNumber.getText().toString();
        String dateIssuing = dateIssuingDriverLicence.getText().toString();
        String dateValid = dateValidUntilDriverLicence.getText().toString();
        DriverLicence driverLicence = new DriverLicence(driverLicenceNum, dateIssuing, dateValid);
        carViewModel.getClientCreditCards().observe(this, strings -> {
            if(strings != null){
                createDriverLicenceAndUpdateClientDriverLicence(driverLicence, driverLicenceNum);
                goToOrderActivity();
            }else{
                createDriverLicenceAndUpdateClientDriverLicence(driverLicence, driverLicenceNum);
                goToAddCreditCardActivity();
            }
        });
    }

    private void createDriverLicenceAndUpdateClientDriverLicence(DriverLicence driverLicence, String driverLicenceNum){
        carViewModel.createDriverLicence(driverLicence);
        carViewModel.updateDriverLicenceClient(driverLicenceNum);
    }

    private void goToOrderActivity(){
        Intent intent = new Intent(AddDriverLicenceActivity.this, OrderCarActivity.class);
        intent.putExtra("DataAboutCar", dataAboutCar);
        startActivity(intent);
    }

    private void goToAddCreditCardActivity(){
        Intent addCreditCard = new Intent(AddDriverLicenceActivity.this, AddCreditCardActivity.class);
        addCreditCard.putExtra("AddCreditCard", dataAboutCar);
        startActivity(addCreditCard);
    }


    public void pickDateIssuingDriverLicence(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> dateIssuingDriverLicence.setText(day + "." + (month + 1) + "." + year), 2023, 7, 24);
        datePickerDialog.show();
    }

    public void pickDateValidUntilDriverLicence(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> dateValidUntilDriverLicence.setText(day + "." + (month + 1) + "." + year), 2023, 7, 24);
        datePickerDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}