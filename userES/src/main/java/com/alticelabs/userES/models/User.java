package com.alticelabs.userES.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties("name")
@Document(collection = "userSnapshots")
public class User {
    private String msisdn;
    private int saldo;
    @JsonIgnore
    private Date timestamp;

    public User() {}

    public User(String id) {
        this.msisdn = id;
    }

    public User(String id, int saldo) {
        this.msisdn = id;
        this.saldo = saldo;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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

    @Override
    public String toString() {
        return "User{" +
                "msisdn='" + msisdn + '\'' +
                ", saldo=" + saldo +
                ", timestamp=" + timestamp +
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
                this.setTimestamp(event.getTimestamp());
            }
        }
    }
}