package com.example.myapplication.rentcarapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.Car;
import com.example.myapplication.rentcarapp.model.firestore.models.Client;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.view.fragment.AccountFragment;
import com.example.myapplication.rentcarapp.view.fragment.HomeFragment;
import com.example.myapplication.rentcarapp.view.fragment.RentFragment;
import com.example.myapplication.rentcarapp.viewmodel.CarViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.List;

public class MainWindowActivity extends AppCompatActivity {
    BottomNavigationView navigationBottom;
    List<Car> cars;
    CarViewModel carViewModel;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        navigationBottom = findViewById(R.id.navigationBottom);
        showFragment(savedInstanceState);
        navigationBottom.setOnItemSelectedListener(this::onItemClicked);
        cars = (List<Car>) getIntent().getSerializableExtra("FilterCar");
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void showFragment(Bundle savedInstanceState){
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, HomeFragment.class, null)
                    .commit();
        }
    }

    public List<Car> getCars() {
        return cars;
    }

    private boolean onItemClicked(MenuItem item){
        if(item.getItemId() == R.id.home){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, HomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
            return true;
        }if(item.getItemId() == R.id.rent){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, RentFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
            return true;
        }if(item.getItemId() == R.id.account){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, AccountFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}