package com.alticelabs.userService.repository;


import com.alticelabs.userService.models.User;
import com.alticelabs.userService.models.UserEvent;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepository {

    private final SnapshotRepository snapshotRepository;
    private final EventRepository eventRepository;

    public UserRepository(SnapshotRepository snapshotRepository, EventRepository eventRepository) {
        this.snapshotRepository = snapshotRepository;
        this.eventRepository = eventRepository;
    }

    public void save(User user) {
        snapshotRepository.save(user);
    }

    public void save(UserEvent userEvent) {
        eventRepository.save(userEvent);
    }

    public User getUser(String id) {
        User latestUserSnapshot = snapshotRepository.findLatestUserSnapshot(id);
        if (latestUserSnapshot == null) return null;
        List<UserEvent> eventsByUser = eventRepository.findEventsByUserAfterTimestamp(id,latestUserSnapshot.getTimestamp());
        latestUserSnapshot.applyEvents(eventsByUser);
        return latestUserSnapshot;
    }

    public User getUserFromPast(String id, Date timestamp) {
        User latestUserSnapshot = snapshotRepository.findLatestUserSnapshotPriorToTimestamp(id,timestamp);
        if (latestUserSnapshot == null) return null;
        List<UserEvent> eventsByUser = eventRepository.findEventsByUserAfterTimestamp(id,latestUserSnapshot.getTimestamp());
        List<UserEvent> eventsPriorToTimestamp = eventsByUser.stream()
                .filter((userEvent -> userEvent.getTimestamp().compareTo(timestamp) <= 0))
                .collect(Collectors.toList());
        latestUserSnapshot.applyEvents(eventsPriorToTimestamp);
        return latestUserSnapshot;
    }

    public List<UserEvent> getEventsFromUser(String id) {
        return eventRepository.findEventsByUser(id);
    }

}
