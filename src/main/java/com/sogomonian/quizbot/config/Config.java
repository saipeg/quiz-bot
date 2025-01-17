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
    @Value("${bot.welcome}")
    private String welcome;
}
