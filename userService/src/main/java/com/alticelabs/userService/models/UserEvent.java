package com.alticelabs.userService.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "userEvents")

public abstract class UserEvent {

    private UserEventType eventType;
    private String userID;
    private Date timestamp;

    public UserEvent(UserEventType eventType) {
        this.eventType = eventType;
    }

    public UserEvent(UserEventType eventType, String userID,Date timestamp) {
        this.eventType = eventType;
        this.userID = userID;
        this.timestamp = timestamp;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "eventType=" + eventType +
                ", userID='" + userID + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
