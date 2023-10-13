package com.example.myapplication.rentcarapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.rentcarapp.R;
import com.example.myapplication.rentcarapp.model.firestore.models.Car;
import com.example.myapplication.rentcarapp.receiver.InternetReceiver;
import com.example.myapplication.rentcarapp.viewmodel.CarViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    ImageView photo;
    TextView carModel, transmissionText, fuelText, engineVolumeText, fuelConsumptionText, childrenChairText, priceText;
    MaterialButton materialButton;
    FloatingActionButton favoriteButton;
    Car car;
    CarViewModel carViewModel;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        photo = findViewById(R.id.imageView7);
        carModel = findViewById(R.id.carModel);
        transmissionText = findViewById(R.id.textView4);
        favoriteButton = findViewById(R.id.favorite);
        fuelText = findViewById(R.id.fuel);
        engineVolumeText = findViewById(R.id.engineVolume);
        fuelConsumptionText = findViewById(R.id.fuelConsumption);
        childrenChairText = findViewById(R.id.childrenChair);
        priceText = findViewById(R.id.price);
        materialButton = findViewById(R.id.rentCar);
        car = (Car) getIntent().getSerializableExtra("Car");
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        broadcastReceiver = new InternetReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        initComponents();
        checkCarByRent();
        checkIsFavorite();
    }

    public void back(View view){
        Intent intent = new Intent(this, MainWindowActivity.class);
        startActivity(intent);
    }

    public void getFavoriteClientsCars(View view){
        carViewModel.getFavoriteClientsCars().observe(this, this::updateFavoriteClientsCars);
    }

    private void updateFavoriteClientsCars(List<String> cars){
        String idCar = car.getID();
        if(!cars.contains(idCar)){
            cars.add(idCar);
            favoriteButton.setImageResource(R.drawable.baseline_favorite_24);
        }else{
            cars.remove(idCar);
            favoriteButton.setImageResource(R.drawable.heart);
        }
        carViewModel.updateFavoriteClientsCars(cars);
    }

    private void initComponents(){
        Picasso.get().load(car.getPhoto()).into(photo);
        transmissionText.setText(car.getTransmission());
        fuelText.setText(car.getTypeOfFuel());
        engineVolumeText.setText(car.getEngineVolume());
        fuelConsumptionText.setText(car.getFuel() + "/100km");
        priceText.setText(car.getPrice() + "/Day");
        isExistChildrenChair();
        getProducerOfModel();
    }

    private void isExistChildrenChair(){
        if(car.isChildrenChair()){
            childrenChairText.setText("Exist");
        }else{
            childrenChairText.setText("Not Exist");
        }
    }

    public void rentCar(View view){
        HashMap<String, Integer> dataAboutCar = new HashMap<>();
        dataAboutCar.put(car.getID(), car.getPrice());
        carViewModel.getClientDriverLicence().observe(this, s -> {
            if(s != null){
                goToOrderCarActivity(dataAboutCar);
            }else{
                goToAddDriverLicence(dataAboutCar);
            }
        });
    }

    private void goToOrderCarActivity(HashMap<String, Integer> dataAboutCar){
        Intent intent = new Intent(DetailActivity.this, OrderCarActivity.class);
        intent.putExtra("DataAboutCar", dataAboutCar);
        startActivity(intent);
    }

    private void goToAddDriverLicence(HashMap<String, Integer> dataAboutCar){
        Intent addDriverLicence = new Intent(DetailActivity.this, AddDriverLicenceActivity.class);
        addDriverLicence.putExtra("AddDriverLicence", dataAboutCar);
        startActivity(addDriverLicence);
    }

    private void getProducerOfModel(){
        String model = car.getModel();
        carViewModel.getProducerByModel(model).observe(this, s -> carModel.setText(s + " " + model));
    }

    private void checkCarByRent(){
        carViewModel.checkCarIfHeWasBook(car.getID()).observe(this, aBoolean -> {
            if(aBoolean != null){
                checkRent(aBoolean);
            }
        });
    }

    private void checkRent(Boolean aBoolean){
        if(aBoolean){
            materialButton.setEnabled(false);
            materialButton.setBackgroundColor(Color.parseColor("#FF0000"));
            materialButton.setText("Booked");
        }
    }

    private void checkIsFavorite(){
        carViewModel.getFavoriteClientsCars().observe(this, strings -> {
            String idCar = car.getID();
            if(strings.contains(idCar)){
                favoriteButton.setImageResource(R.drawable.baseline_favorite_24);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}