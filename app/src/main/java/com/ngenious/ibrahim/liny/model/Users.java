package com.ngenious.ibrahim.liny.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ibrahim on 30/04/17.
 */
@IgnoreExtraProperties
public class Users {
    private String visibility;
    private String displayName;
    private String picture;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String profession;
    private String dateOfBirth;
    private String age;
    private String gender;
    private String country;
    private String city;
    private String about;
    private String facebookId;
    private String twitterId;
    private String googlePlusId;

    public Users() {
    }
    public Users(String displayName, String picture, String email, String dateOfBirth, String age, String gender) {
        this.displayName = displayName;
        this.picture = picture;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.gender = gender;
    }


    public Users(String visibility, String displayName, String picture, String email, String password, String firstName, String lastName, String profession, String dateOfBirth, String age, String gender, String country, String city, String about, String facebookId, String twitterId, String googlePlusId, double mLatitude, double mLongitude) {
        this.visibility = visibility;
        this.displayName = displayName;
        this.picture = picture;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profession = profession;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.city = city;
        this.about = about;
        this.facebookId = facebookId;
        this.twitterId = twitterId;
        this.googlePlusId = googlePlusId;

    }

    public Users(String displayName, String picture, String email, String firstName, String lastName, String facebookId, String twitterId, String googlePlusId) {
        this.displayName = displayName;
        this.picture = picture;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.facebookId = facebookId;
        this.twitterId = twitterId;
        this.googlePlusId = googlePlusId;
    }

    public String getVisibility() {
        return visibility;
    }

    public Users setVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Users setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public Users setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Users setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Users setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Users setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Users setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getProfession() {
        return profession;
    }

    public Users setProfession(String profession) {
        this.profession = profession;
        return this;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public Users setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public String getAge() {
        return age;
    }

    public Users setAge(String age) {
        this.age = age;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Users setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Users setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Users setCity(String city) {
        this.city = city;
        return this;
    }

    public String getAbout() {
        return about;
    }

    public Users setAbout(String about) {
        this.about = about;
        return this;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public Users setFacebookId(String facebookId) {
        this.facebookId = facebookId;
        return this;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public Users setTwitterId(String twitterId) {
        this.twitterId = twitterId;
        return this;
    }

    public String getGooglePlusId() {
        return googlePlusId;
    }

    public Users setGooglePlusId(String googlePlusId) {
        this.googlePlusId = googlePlusId;
        return this;
    }


}
