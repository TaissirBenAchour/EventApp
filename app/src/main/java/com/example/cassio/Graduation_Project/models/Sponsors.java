package com.example.cassio.Graduation_Project.models;



public class Sponsors {

    String businessType;

    public Sponsors(String businessType) {
        this.businessType = businessType;
    }

    public Sponsors() {
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
