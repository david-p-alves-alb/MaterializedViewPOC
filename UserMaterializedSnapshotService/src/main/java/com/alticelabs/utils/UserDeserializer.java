package com.alticelabs.utils;

import com.alticelabs.models.CreateUserEvent;
import com.alticelabs.models.User;
import com.alticelabs.models.UserEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class UserDeserializer implements Deserializer<User> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public User deserialize(String s, byte[] bytes) {
        try {
            return new ObjectMapper().readValue(bytes, User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() {
    }
}
