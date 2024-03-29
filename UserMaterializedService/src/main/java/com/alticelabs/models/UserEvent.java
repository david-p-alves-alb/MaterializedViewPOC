package com.alticelabs.models;

public abstract class UserEvent {

    private UserEventType eventType;
    private String userID;

    public UserEvent(UserEventType eventType) {
        this.eventType = eventType;
    }

    public UserEvent(UserEventType eventType, String userID) {
        this.eventType = eventType;
        this.userID = userID;
    }

    public UserEventType getEventType() {
        return eventType;
    }

    public void setEventType(UserEventType eventType) {
        this.eventType = eventType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "eventType=" + eventType +
                ", userID='" + userID + '\'' +
                '}';
    }
}
