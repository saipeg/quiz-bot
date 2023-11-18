package com.sogomonian.quizbot.service.impl;


import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.repository.QuestionsRepository;
import com.sogomonian.quizbot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionsRepository questionsRepository;

    public static final String REPOSITORY_ANSWER_FAILED_MESSAGE = "К сожалению произошла ошибка, попробуйте позже";

    @Override
    public List<Questions> getAllQuestions() {
        return questionsRepository.findAll();
    }

    public Questions randomQuestion() {
        int allQuestionsSize = getAllQuestions().size();

        if (allQuestionsSize > 1) {
            Questions questions = new Questions();
            questions.setAnswer(REPOSITORY_ANSWER_FAILED_MESSAGE);
            questions.setQuestion(REPOSITORY_ANSWER_FAILED_MESSAGE);
            return questions;
        }

        int idx = (int) (Math.random() * (allQuestionsSize - 1)) + 1;
        return questionsRepository.findById(idx).get();

    }
}
