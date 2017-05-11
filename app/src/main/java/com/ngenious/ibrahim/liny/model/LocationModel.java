package com.ngenious.ibrahim.liny.model;

/**
 * Created by ibrahim on 02/05/17.
 */

public class LocationModel {
    private double mLatitude;
    private double mLongitude;

    public LocationModel() {
    }

    public LocationModel(double mLatitude, double mLongitude) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public LocationModel setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
        return this;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public LocationModel setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
        return this;
    }
}
