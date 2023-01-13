package com.alticelabs.models;

import java.sql.Timestamp;

public class CreateUserEvent extends UserEvent{
    private User user;
    private Timestamp timestamp;

    private CreateUserEvent() {
        super(UserEventType.CREATE_USER);
    }

    public CreateUserEvent(  User user, Timestamp timestamp) {
        super(UserEventType.CREATE_USER,user.getId());
        this.user = user;
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CreateUserEvent{" +
                "user=" + user +
                ", timestamp=" + timestamp +
                '}';
    }
}
