package com.example.myapplication.rentcarapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.Client;
import com.example.myapplication.rentcarapp.model.firestore.models.CreditCard;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.CarViewModel;

import java.util.HashMap;
import java.util.List;

public class AddCreditCardActivity extends AppCompatActivity {
    HashMap<String, Integer> dataAboutCar;
    EditText creditCardNumber, creditCardDate, creditSpecialCode;
    Spinner bankSpinner;
    CarViewModel carViewModel;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_card);
        creditCardNumber = findViewById(R.id.creditCardNumber);
        creditCardDate = findViewById(R.id.creditCardDate);
        creditSpecialCode = findViewById(R.id.creditSpecialCode);
        bankSpinner = findViewById(R.id.bankSpinner);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        dataAboutCar = (HashMap<String, Integer>) getIntent().getSerializableExtra("AddCreditCard");
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        initData();
    }

    public void addCreditCard(View view){
        String creditCard = creditCardNumber.getText().toString();
        carViewModel.isCreditCardNumberWriteCorrect(creditCard).observe(this, aBoolean -> {
            if(aBoolean){
                addCard();
            }else{
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addCard(){
        String bank = bankSpinner.getSelectedItem().toString();
        String creditCardNum = creditCardNumber.getText().toString();
        String cardDate = creditCardDate.getText().toString();
        String cardSpecialCode = creditSpecialCode.getText().toString();
        CreditCard creditCard = new CreditCard(creditCardNum, bank, cardDate, cardSpecialCode);
        if(dataAboutCar != null){
            updateClientCard(creditCard);
            goToOrderCarActivity();
        }else{
            updateClientCard(creditCard);
            goToMyCards();
        }
    }

    private void initData(){
        carViewModel.getBanks().observe(this, strings -> {
            String[] banks = strings.toArray(new String[0]);
            initSpinner(banks);
        });
    }

    private void initSpinner(String[] banks){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, banks);
        bankSpinner.setAdapter(adapter);
    }

    private void updateClientCard(CreditCard creditCard){
        carViewModel.createCreditCard(creditCard);
        carViewModel.getClient().observe(this, client -> update(client.getCards(), client.getToken(), creditCard.getNumberCard()));
    }

    private void goToOrderCarActivity(){
        Intent intent = new Intent(this, OrderCarActivity.class);
        intent.putExtra("DataAboutCar", dataAboutCar);
        startActivity(intent);
    }

    private void goToMyCards(){
        Intent intent = new Intent(this, MyCardsActivity.class);
        startActivity(intent);
    }

    private void update(List<String> cards, String token, String creditCardNumber){
        cards.add(creditCardNumber);
        carViewModel.updateCreditCardClient(cards, token);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}