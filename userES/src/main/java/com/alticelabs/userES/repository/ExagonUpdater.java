package com.alticelabs.userES.repository;

import com.alticelabs.userES.models.CreateUserEvent;
import com.alticelabs.userES.models.User;
import com.alticelabs.userES.models.UserEvent;
import com.alticelabs.userES.models.UserEventType;
import com.alticelabs.userES.utils.UserEventDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
public class ExagonUpdater implements Runnable{
        private final KafkaConsumer<String, UserEvent> userConsumer;
        private final ExagonRepository exagonRepository;
        private final Map<String,Integer> eventCounterForUsers;

        public ExagonUpdater(ExagonRepository exagonRepository) {
            this.userConsumer = new KafkaConsumer<>(configs());
            userConsumer.subscribe(List.of("users"));
            this.exagonRepository = exagonRepository;
            this.eventCounterForUsers = new HashMap<>();
        }


        private Properties configs() {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:19092");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "userESgroup");
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserEventDeserializer.class);
            props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,10000);

            return props;
        }

        @Override
        public void run() {
            while(true) {
                ConsumerRecords<String, UserEvent> consumerRecords = userConsumer.poll(Duration.ofMillis(200));
                for (ConsumerRecord<String, UserEvent> consumerRecord : consumerRecords) {
                    UserEvent userEvent = consumerRecord.value();
                    exagonRepository.save(userEvent);
                    String userMsisdn = userEvent.getMsisdn();

                    if (userEvent.getEventType() == UserEventType.CREATE_USER) {
                        CreateUserEvent createUserEvent = (CreateUserEvent) userEvent;
                        User user = createUserEvent.getUser();
                        user.setTimestamp(userEvent.getTimestamp());
                        eventCounterForUsers.put(userMsisdn,0);
                        exagonRepository.save(user);
                    }
                    else {
                        Integer numberOfEventsFromLastSnapshot = eventCounterForUsers.get(userMsisdn);
                        if (numberOfEventsFromLastSnapshot == 4) {
                            User userById = exagonRepository.findUserById(userMsisdn);
                            exagonRepository.save(userById);
                            eventCounterForUsers.put(userMsisdn, 0);
                        }
                        else eventCounterForUsers.put(userMsisdn, numberOfEventsFromLastSnapshot + 1);
                    }
                }
            }
        }
}
