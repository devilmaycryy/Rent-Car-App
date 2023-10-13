package com.example.myapplication.rentcarapp.model.firestore.models;

public class Station {
    private String name;
    private String address;

    public Station(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
