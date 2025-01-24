package com.sogomonian.quizbot.service.impl;


import com.sogomonian.quizbot.model.User;
import com.sogomonian.quizbot.repository.UserRepository;
import com.sogomonian.quizbot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private Map<Long, Map<String, Integer>> vacantQuestions = new HashMap<>();

    private UserRepository userRepository;
    private List<User> allUsers;
    private JavaQuestionServiceImpl javaQuestionService;
    private KuberQuestionServiceImpl kuberQuestionService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, List<User> allUsers, JavaQuestionServiceImpl javaQuestionService, KuberQuestionServiceImpl kuberQuestionService) {
        this.userRepository = userRepository;
        this.allUsers = allUsers;
        this.javaQuestionService = javaQuestionService;
        this.kuberQuestionService = kuberQuestionService;
    }

    @Override
    public List<User> getAllUsers() {
        try {
            allUsers = userRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Repository not found");
            return new ArrayList<>();
        }

        return allUsers;
    }

    public void addNewChatIdToBase(String chatId) {
        User user = new User();
        user.setChatId(chatId);
        user.setLastTime(LocalDateTime.now());
        userRepository.save(user);
        log.info("Пользователь: " + chatId + " успешно сохранен");
    }

    public Map<Long, Map<String, Integer>> getVacantQuestions() {
        return vacantQuestions;
    }

    public void setVacantQuestions(Map<Long, Map<String, Integer>> vacantQuestions) {
        this.vacantQuestions = vacantQuestions;
    }

    public void addQuestionsForClient(Long chatId) {
        Map<String, Integer> questions = new HashMap<>();
        questions.put("java", javaQuestionService.getAllQuestions().size());
        questions.put("kuber", kuberQuestionService.getAllQuestions().size());
        log.warn("Вопросы для chatID: " + chatId + " добавлены");

        vacantQuestions.put(chatId, questions);
    }
}
