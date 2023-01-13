package com.alticelabs.userES.service;

import com.alticelabs.userES.models.User;
import com.alticelabs.userES.models.UserEvent;
import com.alticelabs.userES.repository.ExagonRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserESService {

    private final ExagonRepository exagonRepository;

    public UserESService(ExagonRepository exagonRepository) {
        this.exagonRepository = exagonRepository;
    }

    public List<UserEvent> getAllEventsForUser(String id) {
        return exagonRepository.findAllEventsForUser(id);
    }

    public User getUser(String id) {
        return exagonRepository.findUserById(id);
    }

    public User getUserFromPast(String id, Date timestamp) {
        return exagonRepository.rebuildUserByIdForTimestamp(id,timestamp);
    }
}
//