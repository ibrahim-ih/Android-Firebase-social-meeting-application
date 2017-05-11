package com.ngenious.ibrahim.liny.model;

import java.util.Date;

/**
 * Created by ibrahim on 02/05/17.
 */

public class Chat {
    private String message;
    private String id;

    public Chat() {
    }

    public Chat(String message, String id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}