package com.sogomonian.quizbot.service.impl;

import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KuberQuestionServiceImpl implements QuestionService {
    @Override
    public List<Questions> getAllQuestions() {
        return null;
    }

    @Override
    public Questions getRandomQuestion() {
        return null;
    }
}
