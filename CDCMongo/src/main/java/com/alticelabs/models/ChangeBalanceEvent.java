package com.alticelabs.models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import java.util.Date;

public class ChangeBalanceEvent extends UserEvent {
    private int value;
    private Operation operation;

    public ChangeBalanceEvent() {
        super(UserEventType.CHANGE_BALANCE);
    }

    public ChangeBalanceEvent(String id, int value,Operation operation, Date timestamp) {
        super(UserEventType.CHANGE_BALANCE,id,timestamp);
        this.value = value;
        this.operation = operation;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return super.toString() + "ChangeBalanceEvent{" +
                "value=" + value +
                ", operation=" + operation +
                '}';
    }
}
