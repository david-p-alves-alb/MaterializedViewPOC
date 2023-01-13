package com.alticelabs.userES.repository;

import com.alticelabs.userES.models.User;
import com.alticelabs.userES.models.UserEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<UserEvent,String> {
    @Query("{ 'msisdn' : ?0 }")
    List<UserEvent> findEventsByUser(String id);

    @Query("{ 'msisdn' : ?0 , 'timestamp' : { $gt: ?1 }}")
    List<UserEvent> findEventsByUserAfterTimestamp(String id, Date timestamp);
}
