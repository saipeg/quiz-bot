package com.sogomonian.quizbot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Config {
    @Value("${bot.token}")
    private String token;
    @Value("${bot.name}")
    private String username;
    @Value("${bot.welcome_for_new_users}")
    private String welcome_for_new_users;
    @Value("${bot.welcome_for_already_users}")
    private String welcome_for_already_users;
}
