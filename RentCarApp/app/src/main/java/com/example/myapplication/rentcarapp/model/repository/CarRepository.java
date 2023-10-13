package com.example.myapplication.rentcarapp.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.rentcarapp.model.firestore.models.Bank;
import com.example.myapplication.rentcarapp.model.firestore.models.Car;
import com.example.myapplication.rentcarapp.model.firestore.models.Client;
import com.example.myapplication.rentcarapp.model.firestore.models.CreditCard;
import com.example.myapplication.rentcarapp.model.firestore.models.DriverLicence;
import com.example.myapplication.rentcarapp.model.firestore.models.Rent;
import com.example.myapplication.rentcarapp.model.firestore.models.Station;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CarRepository {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    public CarRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public LiveData<Client> getClient(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        MutableLiveData<Client> clientMutableLiveData = new MutableLiveData<>();
        if(firebaseUser != null){
            String token = firebaseUser.getUid();
            firestore.collection("clients").document(token).get().addOnCompleteListener(task -> {
                if(task.isSuccessful() && task.getResult().exists()){
                    Log.i("client", "Successful");
                    clientMutableLiveData.setValue(task.getResult().toObject(Client.class));
                }else{
                    Log.i("client", "Unsuccessful");
                    clientMutableLiveData.setValue(null);
                    Log.i("Errors", "Exception:", task.getException());
                }
            });
        }else{
            clientMutableLiveData.setValue(null);
        }
        return clientMutableLiveData;
    }

    public LiveData<List<Car>> getAllCars(){
        MutableLiveData<List<Car>> cars = new MutableLiveData<>();
        firestore.collection("cars").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                cars.setValue(task.getResult().toObjects(Car.class));
            }else{
                cars.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return cars;
    }

    public LiveData<List<Car>> getCarByModel(String model){
        MutableLiveData<List<Car>> cars = new MutableLiveData<>();
        firestore.collection("cars").whereEqualTo("model", model).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                cars.setValue(task.getResult().toObjects(Car.class));
            }else{
                cars.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return cars;
    }

    public LiveData<List<Car>> getCarsById(List<String> ids){
        MutableLiveData<List<Car>> cars = new MutableLiveData<>();
        firestore.collection("cars").whereIn("ID", ids).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    cars.setValue(task.getResult().toObjects(Car.class));
                }else{
                    cars.setValue(null);
                    Log.i("Errors", "Exception:", task.getException());
                }
            }
        });
        return cars;
    }

    public LiveData<List<CreditCard>> getCreditCards(List<String> clientCards){
        MutableLiveData<List<CreditCard>> creditCards = new MutableLiveData<>();
        firestore.collection("creditCards").whereIn("numberCard", clientCards).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                creditCards.setValue(task.getResult().toObjects(CreditCard.class));
            }else{
                creditCards.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return creditCards;
    }


    public LiveData<List<String>> getFavoriteClientsCars(){
        MutableLiveData<List<String>> favoritesCars = new MutableLiveData<>();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            String token = user.getUid();
            firestore.collection("clients").document(token).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    favoritesCars.setValue(Objects.requireNonNull(task.getResult().toObject(Client.class)).getCars());
                }else{
                    Log.i("Errors", "Exception:", task.getException());
                }
            });
        }
        return favoritesCars;
    }

    public void updateFavoriteClientsCars(List<String> cars){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            String token = user.getUid();
            firestore.collection("clients").document(token).update("cars", cars).addOnCompleteListener(task -> {
                if(!task.isSuccessful()){
                    Log.i("Errors", "Exception:", task.getException());
                }
            });
        }
    }

    public LiveData<List<Car>> getCarsByPriceAndChildrenChair(int minPrice, int maxPrice, boolean childrenChair){
        MutableLiveData<List<Car>> cars = new MutableLiveData<>();
        firestore.collection("cars").whereGreaterThanOrEqualTo("price", minPrice).whereLessThanOrEqualTo("price", maxPrice).whereEqualTo("childrenChair", childrenChair).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                cars.setValue(task.getResult().toObjects(Car.class));
            }else{
                cars.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return cars;
    }

    public LiveData<List<Car>> getCarsByAllChoices(int minPrice, int maxPrice, boolean childrenChair, String transmission, String typeOfFuel){
        MutableLiveData<List<Car>> cars = new MutableLiveData<>();
        firestore.collection("cars").whereEqualTo("childrenChair", childrenChair).whereGreaterThanOrEqualTo("price", minPrice).whereLessThanOrEqualTo("price", maxPrice).whereEqualTo("transmission", transmission).whereEqualTo("typeOfFuel", typeOfFuel).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                cars.setValue(task.getResult().toObjects(Car.class));
            }else{
                cars.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return cars;
    }

    public LiveData<List<Car>> getCarsWithoutTransmission(int minPrice, int maxPrice, boolean childrenChair, String typeOfFuel){
        MutableLiveData<List<Car>> cars = new MutableLiveData<>();
        firestore.collection("cars").whereEqualTo("childrenChair", childrenChair).whereGreaterThanOrEqualTo("price", minPrice).whereLessThanOrEqualTo("price", maxPrice).whereEqualTo("typeOfFuel", typeOfFuel).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                cars.setValue(task.getResult().toObjects(Car.class));
            }else{
                cars.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return cars;
    }

    public LiveData<List<Car>> getCarsWithoutTypeOfFuel(int minPrice, int maxPrice, boolean childrenChair, String transmission){
        MutableLiveData<List<Car>> cars = new MutableLiveData<>();
        firestore.collection("cars").whereEqualTo("childrenChair", childrenChair).whereGreaterThanOrEqualTo("price", minPrice).whereLessThanOrEqualTo("price", maxPrice).whereEqualTo("transmission", transmission).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                cars.setValue(task.getResult().toObjects(Car.class));
            }else{
                cars.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return cars;
    }

    public LiveData<String> getClientDriverLicence(){
        MutableLiveData<String> driverLicence = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            String token = firebaseUser.getUid();
            firestore.collection("clients").document(token).get().addOnCompleteListener(task -> {
                if(task.isSuccessful() && task.getResult().exists()){
                    driverLicence.setValue(Objects.requireNonNull(task.getResult().toObject(Client.class)).getDriverLicence());
                }else{
                    driverLicence.setValue(null);
                    Log.i("Errors", "Exception:", task.getException());
                }
            });
        }
        return driverLicence;
    }

    public LiveData<List<String>> getClientCreditCards(){
        MutableLiveData<List<String>> creditsCards = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            String token = firebaseUser.getUid();
            firestore.collection("clients").document(token).get().addOnCompleteListener(task -> {
                if(task.isSuccessful() && task.getResult().exists()){
                    creditsCards.setValue(Objects.requireNonNull(task.getResult().toObject(Client.class)).getCards());
                }else{
                    creditsCards.setValue(null);
                    Log.i("Errors", "Exception:", task.getException());
                }
            });
        }
        return creditsCards;
    }

    public LiveData<String> getProducerByModel(String model){
        MutableLiveData<String> producer = new MutableLiveData<>();
        firestore.collection("models").document(model).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    producer.setValue(Objects.requireNonNull(task.getResult().get("producer")).toString());
                }else{
                    producer.setValue(null);
                    Log.i("Errors", "Exception:", task.getException());
                }
            }
        });
        return producer;
    }

    public LiveData<List<String>> getBanks(){
        MutableLiveData<List<String>> banks = new MutableLiveData<>();
        firestore.collection("banks").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<String> names = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                    names.add(queryDocumentSnapshot.toObject(Bank.class).getName());
                }
                banks.setValue(names);
            }else{
                banks.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return banks;
    }

    public LiveData<List<String>> getStations(){
        MutableLiveData<List<String>> stations = new MutableLiveData<>();
        firestore.collection("stations").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<String> strings = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                    strings.add(queryDocumentSnapshot.toObject(Station.class).getName());
                }
                stations.setValue(strings);
            }else{
                stations.setValue(null);
                Log.i("Errors", "Exception:", task.getException());
            }
        });
        return stations;
    }

    public LiveData<Boolean> checkCarIfHeWasBook(String idCar){
        MutableLiveData<Boolean> isCarWasAddedInRent = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            String token = firebaseAuth.getUid();
            firestore.collection("rents").whereEqualTo("car", idCar).whereEqualTo("client", token).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()){
                        isCarWasAddedInRent.setValue(true);
                    }else{
                        isCarWasAddedInRent.setValue(false);
                    }
                }else{
                    isCarWasAddedInRent.setValue(null);
                    Log.i("Errors", "Exception:", task.getException());
                }
            });
        }
        return isCarWasAddedInRent;
    }

    public LiveData<List<Rent>> getClientRents(){
        MutableLiveData<List<Rent>> rents = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            String token = firebaseAuth.getUid();
            firestore.collection("rents").whereEqualTo("client", token).get().addOnCompleteListener(task -> {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    rents.setValue(task.getResult().toObjects(Rent.class));
                }else{
                    rents.setValue(null);
                    Log.i("Errors", "Exception:", task.getException());
                }
            });
        }
        return rents;
    }

    public void createCreditCard(CreditCard creditCard){
        firestore.collection("creditCards").document(creditCard.getNumberCard()).set(creditCard).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Errors", "Exception:", task.getException());
            }
        });
    }

    public void createDriverLicence(DriverLicence driverLicence){
        firestore.collection("driverslicence").document(driverLicence.getId()).set(driverLicence).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Errors", "Exception:", task.getException());
            }
        });
    }

    public void updateCreditCardClient(List<String> cards, String token){
        firestore.collection("clients").document(token).update("cards", cards).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Errors", "Exception:", task.getException());
            }
        });
    }

    public void updateDriverLicenceClient(String driverLicence){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            String token = user.getUid();
            firestore.collection("clients").document(token).update("driverLicence", driverLicence).addOnCompleteListener(task -> {
                if(!task.isSuccessful()){
                    Log.i("Errors", "Exception:", task.getException());
                }
            });
        }
    }

    public void createRent(Rent rent){
        firestore.collection("rents").document(rent.getId()).set(rent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Log.i("Errors", "Exception:", task.getException());
                }
            }
        });
    }

    public void deleteRent(String idRent){
        firestore.collection("rents").document(idRent).delete().addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Errors", "Exception:", task.getException());
            }
        });
    }
}
