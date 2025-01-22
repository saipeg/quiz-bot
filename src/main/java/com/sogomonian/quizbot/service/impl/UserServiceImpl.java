package com.sogomonian.quizbot.service.impl;


import com.sogomonian.quizbot.model.User;
import com.sogomonian.quizbot.repository.UserRepository;
import com.sogomonian.quizbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private List<User> allUsers;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, List<User> allUsers) {
        this.userRepository = userRepository;
        this.allUsers = allUsers;
    }

    @Override
    public List<User> getAllUsers() {
        try {
            allUsers = userRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Repository not found");
            return new ArrayList<>();
        }

        return allUsers;
    }

    public void addNewChatIdToBase(String chatId) {
        User user = new User();
        user.setChatId(chatId);
        user.setLastTime(LocalDateTime.now());
        userRepository.save(user);
        LOGGER.info("Пользователь: " + chatId + " успешно сохранен");
    }
}
