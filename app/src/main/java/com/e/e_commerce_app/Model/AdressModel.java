package com.e.e_commerce_app.Model;

import java.util.ArrayList;

public class AdressModel {
    ArrayList<String > countrylist=new ArrayList<>();
    ArrayList<String> statelist=new ArrayList<>();
    ArrayList<String> citylist=new ArrayList<>();
    String add_id,adrs,pincode;
    String country,state,city;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdd_id() {
        return add_id;
    }

    public void setAdd_id(String add_id) {
        this.add_id = add_id;
    }

    public  ArrayList<String> getCountrylist() {
        return countrylist;
    }

    public void setCountrylist(ArrayList<String> countrylist) {
        this.countrylist = countrylist;
    }

    public ArrayList<String > getStatelist() {
        return statelist;
    }

    public void setStatelist(ArrayList<String > statelist) {
        this.statelist = statelist;
    }

    public ArrayList<String > getCitylist() {
        return citylist;
    }

    public void setCitylist(ArrayList<String > citylist) {
        this.citylist = citylist;
    }



    public String getAdrs() {
        return adrs;
    }

    public void setAdrs(String adrs) {
        this.adrs = adrs;
    }



    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
