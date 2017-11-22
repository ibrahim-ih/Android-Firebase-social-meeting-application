package com.ngenious.ibrahim.liny.model;

/**
 * Created by ibrahim on 04/06/17.
 */

public class UserCard {
    String displayName;
    String picture;
    boolean seen;

    public UserCard() {
    }

    public UserCard(String displayName, String picture, boolean seen) {
        this.displayName = displayName;
        this.picture = picture;
        this.seen = seen;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserCard setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public UserCard setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public boolean isSeen() {
        return seen;
    }

    public UserCard setSeen(boolean seen) {
        this.seen = seen;
        return this;
    }
}
