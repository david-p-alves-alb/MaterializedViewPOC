package com.alticelabs.models;

public abstract class UserEvent {

    private UserEventType eventType;
    public UserEvent(UserEventType eventType) {
        this.eventType = eventType;
    }

    public UserEventType getEventType() {
        return eventType;
    }

    public void setEventType(UserEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "eventType=" + eventType +
                '}';
    }
}
