package com.example.cassio.Graduation_Project.models;



public class Sponsors {

    String businessType;
    String name;

    public Sponsors(String businessType) {
        this.businessType = businessType;
        this.name=name;
    }

    public Sponsors() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
