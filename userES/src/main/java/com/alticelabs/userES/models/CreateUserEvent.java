package com.alticelabs.userES.models;

import java.sql.Timestamp;
import java.util.Date;

public class CreateUserEvent extends UserEvent{
    private User user;

    public CreateUserEvent() {
        super(UserEventType.CREATE_USER);
    }

    public CreateUserEvent(User user, Timestamp timestamp) {
        super(UserEventType.CREATE_USER,user.getMsisdn(),timestamp);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return super.toString() + "CreateUserEvent{" +
                "user=" + user +
                '}';
    }
}
