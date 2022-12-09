package com.alticelabs.repository;

import com.alticelabs.models.*;
import com.alticelabs.utils.UserEventDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaMaterializedView {

    private final Map<String,User> userSnapshots;
    private final Map<String, List<UserEvent>> userEvents;

    public KafkaMaterializedView() {

        this.userSnapshots = new ConcurrentHashMap<>();
        this.userEvents = new ConcurrentHashMap<>();
        Thread consumerThread = new Thread(new userConsumerThread(userSnapshots, userEvents));
        consumerThread.start();
    }

    public User getUser(String id) {
        User userSnapshot = userSnapshots.get(id);
        if (userSnapshot == null) return null;
        User userToBeBuilt = new User(userSnapshot.getId(),userSnapshot.getSaldo());
        userToBeBuilt.applyEvents(userEvents.get(id));
        return userToBeBuilt;
    }

    public User getUserSnapshot(String id) {
        return userSnapshots.get(id);
    }

    static class userConsumerThread implements Runnable{

        private final Map<String,User> userSnapshots;
        private final Map<String, List<UserEvent>> userEvents;
        private final KafkaConsumer<String,UserEvent> userConsumer;

        public userConsumerThread(Map<String, User> userSnapshots, Map<String, List<UserEvent>> userEvents) {
            this.userSnapshots = userSnapshots;
            this.userEvents = userEvents;
            this.userConsumer = new KafkaConsumer<>(configs());
            userConsumer.subscribe(List.of("users"));
        }

        private Properties configs() {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:19092");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserEventDeserializer.class);

            return props;
        }

        @Override
        public void run() {
            while(true) {
                ConsumerRecords<String, UserEvent> consumerRecords = userConsumer.poll(Duration.ofMillis(200));
                for (ConsumerRecord<String, UserEvent> consumerRecord : consumerRecords) {
                    if (consumerRecord.value().getEventType() == UserEventType.CREATE_USER) {
                        CreateUserEvent createUserEvent = (CreateUserEvent) consumerRecord.value();
                        User user = createUserEvent.getUser();
                        userSnapshots.put(user.getId(),new User(user.getId(),user.getSaldo()));
                        userEvents.put(user.getId(),new ArrayList<>());
                    }
                    else if (consumerRecord.value().getEventType() == UserEventType.CHANGE_BALANCE) {
                        ChangeBalanceEvent changeBalanceEvent = (ChangeBalanceEvent) consumerRecord.value();
                        List<UserEvent> userLastEvents = userEvents.get(consumerRecord.key());
                        userLastEvents.add(changeBalanceEvent);
                        if (userLastEvents.size() == 5) {
                            //Refresh Snapshot
                            User user = userSnapshots.get(consumerRecord.key());
                            user.applyEvents(userEvents.get(user.getId()));
                            userSnapshots.put(user.getId(),user);
                            userLastEvents = new ArrayList<>();
                        }
                        userEvents.put(consumerRecord.key(),userLastEvents);
                    }
                }
            }
        }
    }
}