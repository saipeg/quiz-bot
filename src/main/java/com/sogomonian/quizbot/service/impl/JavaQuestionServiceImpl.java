package com.sogomonian.quizbot.service.impl;


import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.repository.QuestionsRepository;
import com.sogomonian.quizbot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class JavaQuestionServiceImpl implements QuestionService {

    private QuestionsRepository questionsRepository;
    private List<Questions> allQuestions;
    private UserServiceImpl userServiceImpl;

    @Autowired
    public JavaQuestionServiceImpl(QuestionsRepository questionsRepository, List<Questions> allQuestions, @Lazy UserServiceImpl userServiceImpl) {
        this.questionsRepository = questionsRepository;
        this.allQuestions = allQuestions;
        this.userServiceImpl = userServiceImpl;
    }

    @PostConstruct
    public void init() {
        allQuestions = getAllQuestions();
    }

    public Questions getRandomQuestionFor(Long chatId) {

        Map<String, Integer> topicsAndLastQuestions = userServiceImpl.getVacantQuestions().get(chatId);
        Integer lastQuestions = topicsAndLastQuestions.get("java");


        if (lastQuestions > 0) {
            val question = allQuestions.get(lastQuestions - 1);
            lastQuestions -= 1;
            Map<String, Integer> questions = new HashMap<>();
            questions.put("java", lastQuestions);
            userServiceImpl.getVacantQuestions().put(chatId, questions);
            return question;
        } else {
            return null;
        }

    }

    @Override
    public List<Questions> getAllQuestions() {
        List<Questions> questions;
        try {
            questions = questionsRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Repository not found");
            return new ArrayList<>();
        }

        return questions;
    }
}
