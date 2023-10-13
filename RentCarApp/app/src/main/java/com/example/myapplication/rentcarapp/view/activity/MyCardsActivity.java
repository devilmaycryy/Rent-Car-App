package com.example.myapplication.rentcarapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.adapter.CardAdapter;
import com.example.myapplication.rentcarapp.model.firestore.models.CreditCard;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.CarViewModel;

import java.util.List;

public class MyCardsActivity extends AppCompatActivity {
    RecyclerView cardsList;
    CarViewModel carViewModel;
    CardAdapter cardAdapter;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);
        cardsList = findViewById(R.id.cardsList);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        initData();
    }

    public void addCard(View view){
        Intent intent = new Intent(this, AddCreditCardActivity.class);
        startActivity(intent);
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
        cardAdapter = new CardAdapter(creditCards);
        cardsList.setHasFixedSize(true);
        cardsList.setAdapter(cardAdapter);
        cardsList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}