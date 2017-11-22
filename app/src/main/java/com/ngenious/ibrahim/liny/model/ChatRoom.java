package com.ngenious.ibrahim.liny.model;

/**
 * Created by ibrahim on 04/06/17.
 */

public class ChatRoom {
    String id, name, lastMessage;
    long timestamp;
    int unreadCount;

    public ChatRoom() {
    }

    public ChatRoom(String id, String name, String lastMessage, long timestamp, int unreadCount) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
    }

    public ChatRoom(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ChatRoom(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getId() {
        return id;
    }

    public ChatRoom setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ChatRoom setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public ChatRoom setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ChatRoom setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public ChatRoom setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
        return this;
    }
}
