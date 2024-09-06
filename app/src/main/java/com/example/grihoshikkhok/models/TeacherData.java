package com.example.grihoshikkhok.models;

import android.graphics.Bitmap;
import android.media.Image;

public class TeacherData {
    private String name,phone,email,password,gender,address,details,institution,level,department,subject;
    private String id,rating,costrange,total_rating,numof_review,userId;
    private boolean isVarified;
    public TeacherData(){
        //Empty
    }

    public TeacherData(String name, String phone, String email, String password, String gender, String address, String details, String institution, String level, String department,String subject, String id, String rating, String costrange, String total_rating, String numof_review,boolean isVarified,String userId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.details = details;
        this.institution = institution;
        this.level = level;
        this.department = department;
        this.subject=subject;
        this.id = id;
        this.rating = rating;
        this.costrange = costrange;
        this.total_rating = total_rating;
        this.numof_review = numof_review;
        this.isVarified=isVarified;
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCostrange() {
        return costrange;
    }

    public void setCostrange(String costrange) {
        this.costrange = costrange;
    }

    public String getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(String total_rating) {
        this.total_rating = total_rating;
    }

    public String getNumof_review() {
        return numof_review;
    }

    public void setNumof_review(String numof_review) {
        this.numof_review = numof_review;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public boolean isVarified() {
        return isVarified;
    }

    public void setVarified(boolean varified) {
        isVarified = varified;
    }
}

