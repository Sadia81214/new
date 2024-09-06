package com.example.grihoshikkhok.models;

public class Rate {
    String id;
    double rating;
    String raterID;
    String ratedID;

    public Rate() {
    }

    public Rate(String id, double rating, String raterID, String ratedID) {
        this.id = id;
        this.rating = rating;
        this.raterID = raterID;
        this.ratedID = ratedID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getRaterID() {
        return raterID;
    }

    public void setRaterID(String raterID) {
        this.raterID = raterID;
    }

    public String getRatedID() {
        return ratedID;
    }

    public void setRatedID(String ratedID) {
        this.ratedID = ratedID;
    }
}
