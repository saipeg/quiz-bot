package com.sogomonian.quizbot.service.impl;


import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.repository.QuestionsRepository;
import com.sogomonian.quizbot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private static final Logger LOGGER = LogManager.getLogger(QuestionServiceImpl.class);

    private QuestionsRepository questionsRepository;
    private List<Questions> allQuestions;

    @Autowired
    public QuestionServiceImpl(QuestionsRepository questionsRepository, List<Questions> allQuestions) {
        this.questionsRepository = questionsRepository;
        this.allQuestions = allQuestions;
    }

    public Questions getRandomQuestion() {
        allQuestions = getAllQuestions();


        if (allQuestions.size() > 0) {
            System.out.println("=========" + allQuestions.size());
            Random r = new Random();
            int questionId = r.nextInt(allQuestions.size());
            val question = allQuestions.get(questionId);
            allQuestions.remove(questionId);
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
            LOGGER.error("Repository not found");
            return new ArrayList<>();
        }

        return questions;
    }
}
