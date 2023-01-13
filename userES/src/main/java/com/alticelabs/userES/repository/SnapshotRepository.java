package com.alticelabs.userES.repository;

import com.alticelabs.userES.models.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SnapshotRepository extends MongoRepository<User,String> {

    @Aggregation(pipeline = {
            "{ '$match' : { 'msisdn' : ?0 }}",
            "{ '$sort' : { 'timestamp' : -1} }",
            "{ '$limit' : 1}"
    })
    User findLatestUserSnapshot(String id);

    @Aggregation(pipeline = {
            "{ '$match' :{ 'msisdn' : ?0,'timestamp' : { $lt: ?1 }} }",
            "{ '$sort' : { 'timestamp' : -1} }",
            "{ '$limit' : 1}"
    })
    User findLatestUserSnapshotPriorToTimestamp(String id, Date timestamp);
}
