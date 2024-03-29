package com.alticelabs.models;

import java.sql.Timestamp;

public class ChangeBalanceEvent extends UserEvent{
    private int value;
    private Operation operation;
    private Timestamp timestamp;

    private ChangeBalanceEvent() {
        super(UserEventType.CHANGE_BALANCE);
    }

    public ChangeBalanceEvent(String id, int value,Operation operation, Timestamp timestamp) {
        super(UserEventType.CHANGE_BALANCE,id);
        this.value = value;
        this.operation = operation;
        this.timestamp = timestamp;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "ChangeBalanceEvent{" +
                "value=" + value +
                ", operation=" + operation +
                ", timestamp=" + timestamp +
                '}';
    }
}