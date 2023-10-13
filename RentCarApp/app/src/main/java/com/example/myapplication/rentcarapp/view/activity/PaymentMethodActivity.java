package com.example.myapplication.rentcarapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.adapter.CardAdapter;
import com.example.myapplication.rentcarapp.adapter.PaymentMethodAdapter;
import com.example.myapplication.rentcarapp.adapter.PaymentMethodInterface;
import com.example.myapplication.rentcarapp.model.firestore.models.CreditCard;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.CarViewModel;

import java.util.List;

public class PaymentMethodActivity extends AppCompatActivity implements PaymentMethodInterface {
    RecyclerView recyclerView;
    PaymentMethodAdapter paymentMethodAdapter;
    CarViewModel carViewModel;
    List<CreditCard> creditCardsClient;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        recyclerView = findViewById(R.id.cards);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        initData();
    }

    private void initData(){
        carViewModel.getClientCreditCards().observe(this, strings -> {
            if(strings != null){
                getCreditCards(strings);
            }
        });
    }

    private void getCreditCards(List<String> strings){
        carViewModel.getCreditCards(strings).observe(this, this::initRecyclerView);
    }

    private void initRecyclerView(List<CreditCard> creditCards){
        creditCardsClient = creditCards;
        paymentMethodAdapter = new PaymentMethodAdapter(creditCards, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(paymentMethodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void paidCard(View view){
        Intent intent = new Intent(this, SuccessfulActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClickRadioButton(int position) {
        System.out.println(creditCardsClient.get(position).getNumberCard());
    }
}