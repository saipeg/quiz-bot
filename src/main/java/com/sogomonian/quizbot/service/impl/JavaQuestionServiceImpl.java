package com.sogomonian.quizbot.service.impl;


import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.repository.QuestionsRepository;
import com.sogomonian.quizbot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Log4j2
@Service
@RequiredArgsConstructor
public class JavaQuestionServiceImpl implements QuestionService {

    private QuestionsRepository questionsRepository;
    private List<Questions> allQuestions;

    @Autowired
    public JavaQuestionServiceImpl(QuestionsRepository questionsRepository, List<Questions> allQuestions) {
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
            log.error("Repository not found");
            return new ArrayList<>();
        }

        return questions;
    }
}
