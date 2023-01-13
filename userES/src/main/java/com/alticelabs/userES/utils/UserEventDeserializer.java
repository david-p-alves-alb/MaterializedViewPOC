package com.alticelabs.userES.utils;


import com.alticelabs.userES.models.UserEvent;
import com.alticelabs.userES.models.UserEventType;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UserEventDeserializer implements Deserializer<UserEvent> {

    private final CreateUserEventDeserializer createUserEventDeserializer;
    private final ChangeBalanceEventDeserializer changeBalanceEventDeserializer;

    public UserEventDeserializer() {
        this.createUserEventDeserializer = new CreateUserEventDeserializer();
        this.changeBalanceEventDeserializer = new ChangeBalanceEventDeserializer();
    }

    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    public UserEvent deserialize(String s, byte[] bytes) {
        return null;
    }

    public UserEvent deserialize(String topic, Headers headers, byte[] data) {
        String type = "";
        for (Header header : headers.headers("type")) {
            type = new String(header.value(), StandardCharsets.UTF_8);
        }
        if (type.compareTo(UserEventType.CREATE_USER.toString()) == 0) {
            return createUserEventDeserializer.deserialize(topic,data);
        }
        else if (type.compareTo(UserEventType.CHANGE_BALANCE.toString()) == 0) {
            return changeBalanceEventDeserializer.deserialize(topic,data);
        }
        return null;
    }

    public void close() {

    }
}
