package com.alticelabs.userES.utils;

import com.alticelabs.userES.models.CreateUserEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class CreateUserEventDeserializer implements Deserializer<CreateUserEvent> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public CreateUserEvent deserialize(String s, byte[] bytes) {
        try {
            return new ObjectMapper().readValue(bytes,CreateUserEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
