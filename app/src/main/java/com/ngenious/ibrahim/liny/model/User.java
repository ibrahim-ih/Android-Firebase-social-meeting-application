package com.ngenious.ibrahim.liny.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ibrahim on 26/04/17.
 */

public class User implements Parcelable {
   private String visibility;
    private String displayName;
    private Uri picture;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String profession;
    private String dateOfBirth;
    private int age;
    private String gender;
    private String country;
    private String city;
    private String about;
    private String facebookId;
    private String twitterId;
    private String googlePlusId;
    private double mLatitude;
    private double mLongitude;

    public User(String displayName, String email, String firstName,
                String lastName, String googlePlusId, Uri picture, String facebookId) {
        this.displayName = displayName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.googlePlusId = googlePlusId;
        this.picture = picture;
        this.facebookId = facebookId;
    }
    public User() {
    }

    public User(String displayName, String email, String password) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
    }

    public User(Uri picture, String dateOfBirth, int age, String gender) {
        this.picture = picture;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.gender = gender;
    }

    public User(String displayName, Uri picture, String email, String dateOfBirth, int age, String gender) {
        this.displayName = displayName;
        this.picture = picture;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.gender = gender;
    }


    public User(String visibility, String displayName, Uri picture, String firstName, String lastName, String profession, int age, String gender, String country, String city, String about, double mLatitude, double mLongitude) {
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
    public double getmLatitude() {
        return mLatitude;
    }

    public User setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
        return this;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public User setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
        return this;
    }

    public User(double mLatitude, double mLongitude) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }



    public String getVisibility() {
        return visibility;
    }

    public User setVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public User setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Uri getPicture() {
        return picture;
    }

    public User setPicture(Uri picture) {
        this.picture = picture;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getProfession() {
        return profession;
    }

    public User setProfession(String profession) {
        this.profession = profession;
        return this;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public User setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public User setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public User setCity(String city) {
        this.city = city;
        return this;
    }

    public String getAbout() {
        return about;
    }

    public User setAbout(String about) {
        this.about = about;
        return this;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public User setFacebookId(String facebookId) {
        this.facebookId = facebookId;
        return this;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public User setTwitterId(String twitterId) {
        this.twitterId = twitterId;
        return this;
    }

    public String getGooglePlusId() {
        return googlePlusId;
    }

    public User setGooglePlusId(String googlePlusId) {
        this.googlePlusId = googlePlusId;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.visibility);
        dest.writeString(this.displayName);
        dest.writeParcelable(this.picture, flags);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.profession);
        dest.writeString(this.dateOfBirth);
        dest.writeInt(this.age);
        dest.writeString(this.gender);
        dest.writeString(this.country);
        dest.writeString(this.city);
        dest.writeString(this.about);
        dest.writeString(this.facebookId);
        dest.writeString(this.twitterId);
        dest.writeString(this.googlePlusId);
        dest.writeDouble(this.mLatitude);
        dest.writeDouble(this.mLongitude);
    }

    protected User(Parcel in) {
        this.visibility = in.readString();
        this.displayName = in.readString();
        this.picture = in.readParcelable(Uri.class.getClassLoader());
        this.email = in.readString();
        this.password = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.profession = in.readString();
        this.dateOfBirth = in.readString();
        this.age = in.readInt();
        this.gender = in.readString();
        this.country = in.readString();
        this.city = in.readString();
        this.about = in.readString();
        this.facebookId = in.readString();
        this.twitterId = in.readString();
        this.googlePlusId = in.readString();
        this.mLatitude = in.readDouble();
        this.mLongitude = in.readDouble();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
