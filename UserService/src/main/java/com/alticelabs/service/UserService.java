package com.alticelabs.service;

import com.alticelabs.models.*;
import com.alticelabs.repository.UserRepository;
import com.alticelabs.utils.UserEventSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final static StringSerializer keySerializer = new StringSerializer();
    private final static UserEventSerializer valueSerializer = new UserEventSerializer();
    private final UserRepository userRepository;
    private final KafkaProducer<String,UserEvent> kafkaProducer;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:19092");
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                keySerializer.getClass()
                );
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                valueSerializer.getClass());
        this.kafkaProducer = new KafkaProducer<>(configProps);
    }

    public void createUser(User user) {
        //Send user to kafka
        UserEvent userEvent = new CreateUserEvent(user, Timestamp.valueOf(LocalDateTime.now()));
        ProducerRecord<String,UserEvent> createUserRecord = new ProducerRecord<>("users",user.getId(),userEvent);
        createUserRecord.headers().add("type",UserEventType.CREATE_USER.toString().getBytes(StandardCharsets.UTF_8));
        userRepository.createUser(user);
        this.kafkaProducer.send(createUserRecord);
    }

    public User getUser(String id) {
        return userRepository.getUser(id);
    }

    public void creditBalance(String id, int amount) {
        User user = userRepository.getUser(id);
        if (user != null) {
            user.setSaldo(user.getSaldo() + amount);
            UserEvent userEvent = new ChangeBalanceEvent(amount, Operation.CREDIT,Timestamp.valueOf(LocalDateTime.now()));
            ProducerRecord<String,UserEvent> createUserRecord = new ProducerRecord<>("users",user.getId(),userEvent);
            createUserRecord.headers().add("type",UserEventType.CHANGE_BALANCE.toString().getBytes(StandardCharsets.UTF_8));
            userRepository.saveUserEvent(user.getId(),userEvent);
            this.kafkaProducer.send(createUserRecord);
        }
        else System.out.println("Nenhum usuário foi encontrado com o ID pedido!");
    }

    public void debitBalance(String id, int amount) {
        User user = userRepository.getUser(id);
        if (user != null) {
            if (user.getSaldo() >= amount) {
                user.setSaldo(user.getSaldo() - amount);
                UserEvent userEvent = new ChangeBalanceEvent(amount, Operation.DEBIT,Timestamp.valueOf(LocalDateTime.now()));
                ProducerRecord<String,UserEvent> createUserRecord = new ProducerRecord<>("users",user.getId(),userEvent);
                createUserRecord.headers().add("type",UserEventType.CHANGE_BALANCE.toString().getBytes(StandardCharsets.UTF_8));
                userRepository.saveUserEvent(user.getId(),userEvent);
                this.kafkaProducer.send(createUserRecord);
            }
            else System.out.println("O user não tem saldo suficiente para a operação de débito!");
        }
        else System.out.println("Nenhum usuário foi encontrado com o ID pedido!");
    }

    public User getUserSnapshot(String id) {
        return userRepository.getUserSnapshot(id);
    }
}
