package com.alticelabs.models;

public class User {
    private String id;
    private String name;
    private int saldo;

    public User() {}

    public User(String id) {
        this.id = id;
    }

    public User(String id, String name,int saldo) {
        this.id = id;
        this.name = name;
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}
