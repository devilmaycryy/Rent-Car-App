package com.example.myapplication.rentcarapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.rentcarapp.model.firestore.models.Car;
import com.example.myapplication.rentcarapp.model.firestore.models.Client;
import com.example.myapplication.rentcarapp.model.firestore.models.CreditCard;
import com.example.myapplication.rentcarapp.model.firestore.models.DriverLicence;
import com.example.myapplication.rentcarapp.model.firestore.models.Rent;
import com.example.myapplication.rentcarapp.model.firestore.models.Station;
import com.example.myapplication.rentcarapp.model.repository.CarRepository;


import java.util.List;
import java.util.Locale;

public class CarViewModel extends ViewModel {
    private CarRepository carRepository;
    private MutableLiveData<String> carsTransmission = new MutableLiveData<>(null);
    private MutableLiveData<String> carsTypeOfFuel = new MutableLiveData<>(null);
    private MutableLiveData<Boolean> driverLicence = new MutableLiveData<>();
    private MutableLiveData<Boolean> creditCardNumber = new MutableLiveData<>();

    public CarViewModel(){
        carRepository = new CarRepository();
    }

    public LiveData<Client> getClient(){
        return carRepository.getClient();
    }

    public LiveData<List<Car>> getAllCars(){
        return carRepository.getAllCars();
    }

    public LiveData<List<Car>> getCarByModel(String model){
        return carRepository.getCarByModel(model);
    }

    public LiveData<List<Car>> getCarsById(List<String> ids){
        return carRepository.getCarsById(ids);
    }

    public LiveData<List<String>> getFavoriteClientsCars(){
        return carRepository.getFavoriteClientsCars();
    }

    public LiveData<List<String>> getClientCreditCards(){
        return carRepository.getClientCreditCards();
    }

    public LiveData<List<CreditCard>> getCreditCards(List<String> clientCards){
        return carRepository.getCreditCards(clientCards);
    }

    public LiveData<List<Rent>> getClientRents(){
        return carRepository.getClientRents();
    }

    public LiveData<List<String>> getBanks(){
        return carRepository.getBanks();
    }

    public LiveData<String> getClientDriverLicence(){
        return carRepository.getClientDriverLicence();
    }

    public LiveData<String> getProducerByModel(String model){
        return carRepository.getProducerByModel(model);
    }

    public LiveData<Boolean> checkCarIfHeWasBook(String idCar){
        return carRepository.checkCarIfHeWasBook(idCar);
    }

    public LiveData<List<String>> getStations(){
        return carRepository.getStations();
    }

    public void updateCreditCardClient(List<String> cards, String token){
        carRepository.updateCreditCardClient(cards, token);
    }

    public void updateDriverLicenceClient(String driverLicence){
        carRepository.updateDriverLicenceClient(driverLicence);
    }
    public void updateFavoriteClientsCars(List<String> cars){
        carRepository.updateFavoriteClientsCars(cars);
    }

    public void createCreditCard(CreditCard creditCard){
        carRepository.createCreditCard(creditCard);
    }

    public void createDriverLicence(DriverLicence driverLicence){
        carRepository.createDriverLicence(driverLicence);
    }

    public void createRent(Rent rent){
        carRepository.createRent(rent);
    }

    public void deleteRent(String idRent){
        carRepository.deleteRent(idRent);
    }

    public LiveData<Boolean> isDriverLicenceNumberWriteCorrect(String driverLicenceNumber){
        if(isDriverLicenceNumberCorrect(driverLicenceNumber)){
            driverLicence.setValue(true);
        }else{
            driverLicence.setValue(false);
        }
        return driverLicence;
    }

    public LiveData<Boolean> isCreditCardNumberWriteCorrect(String creditCard){
        if(isCreditCardNumberLengthEqualNineteen(creditCard)){
            creditCardNumber.setValue(true);
        }else{
            creditCardNumber.setValue(false);
        }
        return creditCardNumber;
    }

    public LiveData<String> choiceTransmission(boolean automaton, boolean mechanic){
        if(automaton){
            carsTransmission.setValue("Automaton");
        }if(mechanic){
            carsTransmission.setValue("Mechanic");
        }
        return carsTransmission;
    }

    public LiveData<String> choiceTypeOfFuel(boolean gasoline, boolean diesel){
        if(gasoline){
            carsTypeOfFuel.setValue("Gasoline");
        }if(diesel){
            carsTypeOfFuel.setValue("Diesel");
        }
        return carsTypeOfFuel;
    }

    public LiveData<List<Car>> confirmChoice(boolean childrenChair, String transmission, String typeOfFuel, int minPrice, int maxPrice){
        if(isTransmissionEqualNull(transmission, typeOfFuel)){
            return carRepository.getCarsWithoutTransmission(minPrice, maxPrice, childrenChair, typeOfFuel);
        }if(isTypeOfFuelEqualNull(transmission, typeOfFuel)){
            return carRepository.getCarsWithoutTypeOfFuel(minPrice, maxPrice, childrenChair, transmission);
        }if(isTransmissionAndTypeOfFuelEqualNull(transmission, typeOfFuel)){
            return carRepository.getCarsByPriceAndChildrenChair(minPrice, maxPrice, childrenChair);
        }
        return carRepository.getCarsByAllChoices(minPrice, maxPrice, childrenChair, transmission, typeOfFuel);
    }

    private boolean isTransmissionEqualNull(String transmission, String typeOfFuel){
        return transmission == null && typeOfFuel != null;
    }

    private boolean isTypeOfFuelEqualNull(String transmission, String typeOfFuel){
        return transmission != null && typeOfFuel == null;
    }

    private boolean isTransmissionAndTypeOfFuelEqualNull(String transmission, String typeOfFuel){
        return transmission == null && typeOfFuel == null;
    }

    private boolean isDriverLicenceNumberCorrect(String driverLicenceNumber){
        return driverLicenceNumber.length() == 9;
    }

    private boolean isCreditCardNumberLengthEqualNineteen(String creditCard){
        return creditCard.length() == 19 && creditCard.contains(" ");
    }

}
