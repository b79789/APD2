package com.brianstacks.sportsupclose;

import java.io.Serializable;

/**
 * Created by Brian Stacks
 * on 3/6/15
 * for FullSail.edu.
 */
public class GooglePlace implements Serializable{

    private static final long serialVersionUID = 453332330552888L;

    private String name;
    private String category;
    private String rating;
    private String open;
    private double mLat;
    private double mLong;
    private String distance;
    private String address;

    public GooglePlace() {
        this.name = "";
        this.rating = "";
        this.open = "";
        this.setCategory("");
        this.mLat=0;
        this.mLong=0;
        this.address="";
        this.distance="";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setRating(String rating) {
        this.rating = rating;

    }

    public String getRating() {
        return rating;
    }

    public void setOpenNow(String open) {
        this.open = open;
    }

    public String getOpenNow() {
        return open;
    }

    public double getLat(){
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon(){
        return mLong;
    }

    public void setLon(double lon) {
        mLong = lon;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setDistance(String distance1){
        this.distance=distance1;
    }
    public String getDistance(){
        return distance;
    }
}

