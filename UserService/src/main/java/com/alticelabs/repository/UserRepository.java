package com.alticelabs.repository;

import com.alticelabs.models.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private final Map<String, User> users;

    public UserRepository() {
        this.users = new HashMap<String, User>();
    }

    public UserRepository(Map<String, User> users) {
        this.users = users;
    }

    public void saveUser(User user) {
        this.users.put(user.getId(),user);
    }

    public User getUser(String id) {
        if (users.containsKey(id)) {
            return this.users.get(id);
        }
        return null;
    }
}
