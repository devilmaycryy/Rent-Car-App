package com.example.myapplication.rentcarapp.model.firestore.models;

public class CreditCard {
    private String numberCard;
    private String bank;
    private String validDate;
    private String specialCode;

    public CreditCard(){}

    public CreditCard(String numberCard, String bank, String validDate, String specialCode) {
        this.numberCard = numberCard;
        this.bank = bank;
        this.validDate = validDate;
        this.specialCode = specialCode;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public void setNumberCard(String numberCard) {
        this.numberCard = numberCard;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getSpecialCode() {
        return specialCode;
    }

    public void setSpecialCode(String specialCode) {
        this.specialCode = specialCode;
    }
}
