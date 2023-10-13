package com.example.myapplication.rentcarapp.model.firestore.models;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private String token;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String user;
    private String driverLicence;
    private String photo;
    private List<String> cards = new ArrayList<>();
    private List<String> cars = new ArrayList<>();

    public Client(){}

    public Client(String fullName, String phoneNumber, String email, String user, String photo) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.user = user;
        this.photo = photo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDriverLicence() {
        return driverLicence;
    }

    public void setDriverLicence(String driverLicence) {
        this.driverLicence = driverLicence;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public List<String> getCars() {
        return cars;
    }

    public void setCars(List<String> cars) {
        this.cars = cars;
    }
}
