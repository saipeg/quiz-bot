package com.sogomonian.quizbot.service.impl;


import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.repository.QuestionsRepository;
import com.sogomonian.quizbot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private static final Logger LOGGER = LogManager.getLogger(QuestionServiceImpl.class);

    private QuestionsRepository questionsRepository;

    @Autowired
    public QuestionServiceImpl(QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
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

    public Questions randomQuestion() {
        int allQuestionsSize = getAllQuestions().size();

        if (allQuestionsSize <= 1) {
            Questions questions = new Questions();
            questions.setAnswer(REPOSITORY_ANSWER_FAILED_MESSAGE);
            LOGGER.error("Repository not found");
            return questions;
        }

        int idx = (int) (Math.random() * (allQuestionsSize - 1)) + 1;
        return questionsRepository.findById(idx).get();
//        Questions questions = questionsRepository.findById(idx).get();
//        questions = questionsRepository.findById(idx).get();
//        questions.setAnswer(); = questionsRepository.findById(idx).get();

    }
}
