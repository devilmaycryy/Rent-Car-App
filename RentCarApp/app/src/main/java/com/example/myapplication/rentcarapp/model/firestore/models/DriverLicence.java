package com.example.myapplication.rentcarapp.model.firestore.models;

public class DriverLicence {
    private String id;
    private String dateIssuing;
    private String validDate;

    public DriverLicence(){}

    public DriverLicence(String id, String dateIssuing, String validDate) {
        this.id = id;
        this.dateIssuing = dateIssuing;
        this.validDate = validDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateIssuing() {
        return dateIssuing;
    }

    public void setDateIssuing(String dateIssuing) {
        this.dateIssuing = dateIssuing;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }
}
