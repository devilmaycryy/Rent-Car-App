package com.example.myapplication.rentcarapp.model.firestore.models;

import java.io.Serializable;

public class Rent implements Serializable {
    private String id;
    private String client;
    private String car;
    private String stationIssuing;
    private String stationReturn;
    private String rentalDate;
    private String issuingDate;
    private String returnDate;
    private int fines;
    private int sum;
    private int totalAmount;
    private String status;

    public Rent(){}

    public Rent(String id, String client, String car, String stationIssuing, String stationReturn, String rentalDate, String issuingDate, String returnDate, int sum, int totalAmount) {
        this.id = id;
        this.client = client;
        this.car = car;
        this.stationIssuing = stationIssuing;
        this.stationReturn = stationReturn;
        this.rentalDate = rentalDate;
        this.issuingDate = issuingDate;
        this.returnDate = returnDate;
        this.sum = sum;
        this.totalAmount = totalAmount;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getStationIssuing() {
        return stationIssuing;
    }

    public void setStationIssuing(String stationIssuing) {
        this.stationIssuing = stationIssuing;
    }

    public String getStationReturn() {
        return stationReturn;
    }

    public void setStationReturn(String stationReturn) {
        this.stationReturn = stationReturn;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public String getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(String issuingDate) {
        this.issuingDate = issuingDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getFines() {
        return fines;
    }

    public void setFines(int fines) {
        this.fines = fines;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
