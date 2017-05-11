package com.ngenious.ibrahim.liny.model;

/**
 * Created by ibrahim on 05/05/17.
 */

public class Destination {
    private String from;
    private String to;
    private String placeName;
    private String placeType;
    private String city;


    public Destination() {
    }

    public Destination(String from, String to, String placeName, String placeType, String city) {
        this.from = from;
        this.to = to;
        this.placeName = placeName;
        this.placeType = placeType;
        this.city = city;
    }

    public String getFrom() {
        return from;
    }

    public Destination setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public Destination setTo(String to) {
        this.to = to;
        return this;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Destination setPlaceName(String placeName) {
        this.placeName = placeName;
        return this;
    }

    public String getPlaceType() {
        return placeType;
    }

    public Destination setPlaceType(String placeType) {
        this.placeType = placeType;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Destination setCity(String city) {
        this.city = city;
        return this;
    }
}
