package com.example.myapplication.rentcarapp.model.firestore.models;

import java.io.Serializable;

public class Car implements Serializable {
    private String ID;
    private String model;
    private String transmission;
    private String typeOfFuel;
    private int price;
    private String photo;
    private boolean childrenChair;
    private double fuel;
    private String engineVolume;

    public Car(){}

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getTypeOfFuel() {
        return typeOfFuel;
    }

    public void setTypeOfFuel(String typeOfFuel) {
        this.typeOfFuel = typeOfFuel;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isChildrenChair() {
        return childrenChair;
    }

    public void setChildrenChair(boolean childrenChair) {
        this.childrenChair = childrenChair;
    }

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public String getEngineVolume() {
        return engineVolume;
    }

    public void setEngineVolume(String engineVolume) {
        this.engineVolume = engineVolume;
    }
}
