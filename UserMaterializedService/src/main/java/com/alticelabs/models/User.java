package com.alticelabs.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("name")
public class User {
    private String id;
    private int saldo;
    private long timeToUpdate;

    public User() {}

    public User(String id) {
        this.id = id;
    }

    public User(String id, int saldo) {
        this.id = id;
        this.saldo = saldo;
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

    public long getTimeToUpdate() {
        return timeToUpdate;
    }

    public void setTimeToUpdate(long timeToUpdate) {
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
}
