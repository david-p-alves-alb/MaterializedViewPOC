package com.alticelabs.userService.service;


import com.alticelabs.userService.models.*;
import com.alticelabs.userService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        UserEvent userEvent = new CreateUserEvent(user, Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
        userRepository.save(userEvent);
    }

    public User getUser(String id) {
        return userRepository.getUser(id);
    }

    public Date creditBalance(String id, int amount) {
        User user = userRepository.getUser(id);
        Date eventTimestamp = Timestamp.valueOf(LocalDateTime.now());
        if (user != null) {
            UserEvent userEvent = new ChangeBalanceEvent(user.getMsisdn(),amount, Operation.CREDIT,eventTimestamp);
            userRepository.save(userEvent);
        }
        else System.out.println("Nenhum usuário foi encontrado com o ID pedido!");
        return eventTimestamp;
    }

    public Date debitBalance(String id, int amount) {
        User user = userRepository.getUser(id);
        Date eventTimestamp = Timestamp.valueOf(LocalDateTime.now());
        if (user != null) {
            if (user.getSaldo() >= amount) {
                UserEvent userEvent = new ChangeBalanceEvent(user.getMsisdn(),amount, Operation.DEBIT,eventTimestamp);
                userRepository.save(userEvent);
            }
            else System.out.println("O user não tem saldo suficiente para a operação de débito!");
        }
        else System.out.println("Nenhum usuário foi encontrado com o ID pedido!");
        return eventTimestamp;
    }

    public User getUserFromPast(String id, Date timestamp) {
        return userRepository.getUserFromPast(id,timestamp);
    }

    public List<UserEvent> getHistoryFromUser(String id) {
        return userRepository.getEventsFromUser(id);
    }
}
