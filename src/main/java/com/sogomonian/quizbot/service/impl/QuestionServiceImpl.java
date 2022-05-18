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

    @Override
    public List<Questions> getAllQuestions() {
        List<Questions> questions = questionsRepository.findAll();
        return questions;
    }
}
