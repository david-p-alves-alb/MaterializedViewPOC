package com.alticelabs.userES.models;

import com.alticelabs.userES.models.UserEventType;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "userEvents")
public abstract class UserEvent {

    private UserEventType eventType;
    @JsonAlias("userID")
    private String msisdn;
    private Date timestamp;

    public UserEvent(UserEventType eventType) {
        this.eventType = eventType;
    }

    public UserEvent(UserEventType eventType, String msisdn,Date timestamp) {
        this.eventType = eventType;
        this.msisdn = msisdn;
        this.timestamp = timestamp;
    }

    public UserEventType getEventType() {
        return eventType;
    }

    public void setEventType(UserEventType eventType) {
        this.eventType = eventType;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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
                ", msisdn='" + msisdn + '\'' +
                ", timestamp=" + timestamp.getTime() +
                ',';
    }
}
