package com.ngenious.ibrahim.liny.model;

/**
 * Created by ibrahim on 12/05/17.
 */

public class PlacesModel {
    private String id;
    private String icon;
    private String name;
    private String rate;
    private String type;
    private String vicinity;

    public PlacesModel() {
    }

    public PlacesModel(String id, String icon, String name, String rate, String type, String vicinity) {
        this.id = id;
        this.icon = icon;
        this.rate = rate;
        this.name = name;
        this.type = type;
        this.vicinity = vicinity;
    }

    public String getId() {
        return id;
    }

    public PlacesModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public PlacesModel setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlacesModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public PlacesModel setType(String type) {
        this.type = type;
        return this;
    }

    public String getRate() {
        return rate;
    }

    public PlacesModel setRate(String rate) {
        this.rate = rate;
        return this;
    }

    public String getVicinity() {
        return vicinity;
    }

    public PlacesModel setVicinity(String vicinity) {
        this.vicinity = vicinity;
        return this;
    }
}
