package com.alticelabs.userES.repository;

import com.alticelabs.userES.models.User;
import com.alticelabs.userES.models.UserEvent;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExagonRepository {
    private final EventRepository userEventRepo;
    private final SnapshotRepository userSnapshotRepo;

    public ExagonRepository(EventRepository userEventRepo, SnapshotRepository userSnapshotRepo) {
        this.userEventRepo = userEventRepo;
        this.userSnapshotRepo = userSnapshotRepo;
    }

    public void save(UserEvent userEvent) {
        userEventRepo.save(userEvent);
    }

    public void save(User user) {
        userSnapshotRepo.save(user);
    }

    public List<UserEvent> findAllEventsForUser(String id) {
        return userEventRepo.findEventsByUser(id);
    }

    public User findUserById(String id) {
        User latestUserSnapshot = userSnapshotRepo.findLatestUserSnapshot(id);
        if (latestUserSnapshot == null) return null;
        List<UserEvent> eventsByUser = userEventRepo.findEventsByUserAfterTimestamp(id,latestUserSnapshot.getTimestamp());
        latestUserSnapshot.applyEvents(eventsByUser);
        return latestUserSnapshot;
    }

    public User rebuildUserByIdForTimestamp(String id, Date timestamp) {
        User latestUserSnapshot = userSnapshotRepo.findLatestUserSnapshotPriorToTimestamp(id,timestamp);
        if (latestUserSnapshot == null) return null;
        List<UserEvent> eventsByUser = userEventRepo.findEventsByUserAfterTimestamp(id,latestUserSnapshot.getTimestamp());
        List<UserEvent> eventsPriorToTimestamp = eventsByUser.stream()
                .filter((userEvent -> userEvent.getTimestamp().compareTo(timestamp) <= 0))
                .collect(Collectors.toList());
        latestUserSnapshot.applyEvents(eventsPriorToTimestamp);
        return latestUserSnapshot;
    }
}
