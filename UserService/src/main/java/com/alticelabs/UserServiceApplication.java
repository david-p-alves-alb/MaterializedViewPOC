package com.alticelabs;

import com.alticelabs.controller.UserController;
import com.alticelabs.repository.UserRepository;
import com.alticelabs.service.UserService;

public class UserServiceApplication {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UserController controller = new UserController(userService);
        controller.run();
    }
}