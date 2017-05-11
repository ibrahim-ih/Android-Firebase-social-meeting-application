package com.ngenious.ibrahim.liny.model;

/**
 * Created by ibrahim on 02/05/17.
 */

public class Friend {
    private String visibility;
    private String displayName;
    private String picture;
    private String firstName;
    private String lastName;
    private String profession;
    private int age;
    private String gender;
    private String country;
    private String city;
    private String about;
    private double mLatitude;
    private double mLongitude;

    public Friend() {
    }

    public Friend(String visibility, String displayName, String picture, String firstName, String lastName, String profession, int age, String gender, String country, String city, String about, double mLatitude, double mLongitude) {
        this.visibility = visibility;
        this.displayName = displayName;
        this.picture = picture;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profession = profession;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.city = city;
        this.about = about;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public Friend(String displayName, String picture) {
        this.displayName = displayName;
        this.picture = picture;
    }

    public String getVisibility() {
        return visibility;
    }

    public Friend setVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Friend setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public Friend setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Friend setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Friend setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getProfession() {
        return profession;
    }

    public Friend setProfession(String profession) {
        this.profession = profession;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Friend setAge(int age) {
        this.age = age;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Friend setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Friend setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Friend setCity(String city) {
        this.city = city;
        return this;
    }

    public String getAbout() {
        return about;
    }

    public Friend setAbout(String about) {
        this.about = about;
        return this;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public Friend setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
        return this;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public Friend setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
        return this;
    }
}
