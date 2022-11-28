package com.alticelabs.repository;

import com.alticelabs.models.*;
import com.alticelabs.utils.*;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Properties;

public class KafkaMaterializedView {

    private final KafkaStreams kafkaStreams;
    public KafkaMaterializedView() {

        StreamsBuilder builder = new StreamsBuilder();
        materializeUser(builder);

        this.kafkaStreams = new KafkaStreams(builder.build(),configs());
        this.kafkaStreams.start();
    }

    private void materializeUser(StreamsBuilder builder) {
        Serde<User> userSerde = Serdes.serdeFrom(new UserSerializer(),new UserDeserializer());
        KStream<String, UserEvent> usersKStream = builder.stream("users");
        usersKStream.groupByKey().aggregate(User::new,USER_AGGREGATOR,
                Materialized.<String,User, KeyValueStore<Bytes,byte[]>>as("mv_users")
                            .withKeySerde(Serdes.String())
                            .withValueSerde(userSerde));
    }

    private Properties configs() {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:19092");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,"usersMV");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,UserEventSerde.class);

        return props;
    }

    private final Aggregator<String,UserEvent,User> USER_AGGREGATOR = (key, event, user) -> {
      if (event.getEventType() == UserEventType.CREATE_USER) {
          CreateUserEvent createUserEvent = (CreateUserEvent) event;
          user = createUserEvent.getUser();
          user.setTimeToUpdate(Timestamp.valueOf(LocalDateTime.now()).getTime() - createUserEvent.getTimestamp().getTime());
      }
      else if (event.getEventType() == UserEventType.CHANGE_BALANCE) {
          ChangeBalanceEvent changeBalanceEvent = (ChangeBalanceEvent) event;
          int amount = changeBalanceEvent.getValue();
          if (changeBalanceEvent.getOperation() == Operation.CREDIT)
              user.setSaldo(user.getSaldo() + amount);
          else
              user.setSaldo(user.getSaldo() - amount);
          user.setTimeToUpdate(Timestamp.valueOf(LocalDateTime.now()).getTime() - changeBalanceEvent.getTimestamp().getTime());
      }
      return user;
    };

    public User getUser(String id) {
        ReadOnlyKeyValueStore<String, User> mv_users = kafkaStreams
                .store(StoreQueryParameters
                        .fromNameAndType("mv_users", QueryableStoreTypes.keyValueStore()));
        return mv_users.get(id);
    }
}
