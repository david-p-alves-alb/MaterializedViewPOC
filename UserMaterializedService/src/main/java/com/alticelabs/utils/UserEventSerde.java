package com.alticelabs.utils;

import com.alticelabs.models.UserEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserEventSerde implements Serde<UserEvent> {
    private final UserEventSerializer userEventSerializer;
    private final UserEventDeserializer userEventDeserializer;

    public UserEventSerde() {
        this.userEventDeserializer = new UserEventDeserializer();
        this.userEventSerializer = new UserEventSerializer();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serde.super.configure(configs, isKey);
    }

    @Override
    public void close() {
        Serde.super.close();
    }

    @Override
    public Serializer<UserEvent> serializer() {
        return userEventSerializer;
    }

    @Override
    public Deserializer<UserEvent> deserializer() {
        return userEventDeserializer;
    }
}
