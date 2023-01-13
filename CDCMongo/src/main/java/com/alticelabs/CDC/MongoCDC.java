package com.alticelabs.CDC;

import com.alticelabs.models.*;
import com.alticelabs.utils.UserEventSerializer;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MongoCDC {
    private final MongoDatabase mongoDatabase;
    private final KafkaProducer<String,UserEvent> kafkaProducer;

    public MongoCDC(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
        this.kafkaProducer = new KafkaProducer<>(producerConfigs());
    }

    public void startCDC() {
        MongoCollection<Document> events = mongoDatabase.getCollection("userEvents", Document.class);

        ChangeStreamIterable<Document> changeStreamDocuments = events.watch();
        changeStreamDocuments.map((document) -> {
            Document eventDocument = document.getFullDocument();
            assert eventDocument != null;
            return convertDocumentIntoUserEvent(eventDocument);
        }).forEach((Consumer<? super UserEvent>) userEvent -> sendUserEventToKafka(userEvent,kafkaProducer));
    }

    private void sendUserEventToKafka(UserEvent userEvent,KafkaProducer<String,UserEvent> kafkaProducer) {
        ProducerRecord<String,UserEvent> userEventProducerRecord = new ProducerRecord<>("users",userEvent.getUserID(),userEvent);
        userEventProducerRecord.headers().add("type", userEvent.getEventType().toString().getBytes(StandardCharsets.UTF_8));
        kafkaProducer.send(userEventProducerRecord);
    }

    private UserEvent convertDocumentIntoUserEvent(Document eventDocument) {
        UserEvent userEvent = null;
        UserEventType eventType = UserEventType.valueOf(eventDocument.get("eventType", String.class));
        String userID = eventDocument.get("userID", String.class);
        Date timestamp = eventDocument.get("timestamp", Date.class);
        if (eventType == UserEventType.CREATE_USER) {
            Document userDocument = eventDocument.get("user", Document.class);
            String msisdn = userDocument.get("msisdn", String.class);
            String name = userDocument.get("name", String.class);
            Integer saldo = userDocument.get("saldo", Integer.class);
            Date userTimestamp = userDocument.get("timestamp", Date.class);
            User user = new User(msisdn,name,saldo,userTimestamp);
            userEvent = new CreateUserEvent(user,timestamp);
        }
        else {
            Integer value = eventDocument.get("value", Integer.class);
            Operation operation = Operation.valueOf(eventDocument.get("operation", String.class));
            userEvent = new ChangeBalanceEvent(userID,value,operation,timestamp);
        }
        return userEvent;
    }

    private Map<String,Object> producerConfigs() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:19092");
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                UserEventSerializer.class);
        return configProps;
    }
}
