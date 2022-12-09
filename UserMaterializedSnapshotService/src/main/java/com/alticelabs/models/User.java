package com.alticelabs.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties("name")
public class User {
    private String id;
    private int saldo;
    private LocalDateTime timeToUpdate;

    public User() {}

    public User(String id) {
        this.id = id;
    }

    public User(String id, int saldo) {
        this.id = id;
        this.saldo = saldo;
        this.timeToUpdate = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public LocalDateTime getTimeToUpdate() {
        return timeToUpdate;
    }

    public void setTimeToUpdate(LocalDateTime timeToUpdate) {
        this.timeToUpdate = timeToUpdate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", saldo=" + saldo +
                ", timeToUpdate=" + timeToUpdate +
                '}';
    }

    public void applyEvents(List<UserEvent> userEvents) {
        for (UserEvent event : userEvents) {
            if (event.getEventType() == UserEventType.CHANGE_BALANCE) {
                ChangeBalanceEvent changeBalanceEvent = (ChangeBalanceEvent) event;
                int amount = changeBalanceEvent.getValue();
                if (changeBalanceEvent.getOperation() == Operation.CREDIT)
                    this.setSaldo(this.getSaldo() + amount);
                else
                    this.setSaldo(this.getSaldo() - amount);
            }
        }
        setTimeToUpdate(LocalDateTime.now());
    }
}