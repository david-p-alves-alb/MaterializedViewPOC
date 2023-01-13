package com.alticelabs.userService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "userSnapshots")
public class User {
    private String msisdn;
    private String name;
    private int saldo;
    @JsonIgnore
    private Date timestamp;

    public User() {}

    public User(String id) {
        this.msisdn = id;
    }

    public User(String id, String name,int saldo) {
        this.msisdn = id;
        this.name = name;
        this.saldo = saldo;
        this.timestamp = new Date();
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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
                this.setTimestamp(event.getTimestamp());
            }
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "msisdn='" + msisdn + '\'' +
                ", name='" + name + '\'' +
                ", saldo=" + saldo +
                ", timestamp=" + timestamp +
                '}';
    }
}
