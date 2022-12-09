package com.alticelabs.repository;

import com.alticelabs.models.User;
import com.alticelabs.models.UserEvent;
import com.alticelabs.models.UserEventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {

    private final Map<String,User> userSnapshots;
    private final Map<String, List<UserEvent>> userEvents;

    public UserRepository() {
        this.userSnapshots = new HashMap<>();
        this.userEvents = new HashMap<>();
    }

    public void createUser(User user) {
        userSnapshots.put(user.getId(),user);
        userEvents.put(user.getId(),new ArrayList<>());
    }
    public void saveUserEvent(String id,UserEvent userEvent) {
        List<UserEvent> userEventList = userEvents.get(id);
        userEventList.add(userEvent);
        if (userEventList.size() == 5) {
            User userSnapshot = this.userSnapshots.get(id);
            userSnapshot.applyEvents(userEventList);
            userEventList = new ArrayList<>();
        }
        userEvents.put(id,userEventList);
    }

    public User getUser(String id) {
        User userSnapshot = userSnapshots.get(id);
        if (userSnapshot == null) return null;
        User userToBeBuilt = new User(userSnapshot.getId(),userSnapshot.getName(),userSnapshot.getSaldo());
        userToBeBuilt.applyEvents(userEvents.get(id));
        return userToBeBuilt;
    }

    public User getUserSnapshot(String id) {
        return userSnapshots.get(id);
    }
}
