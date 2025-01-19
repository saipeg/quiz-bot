package com.sogomonian.quizbot.service;

import com.sogomonian.quizbot.model.Questions;

import java.util.List;

public interface QuestionService {
    String REPOSITORY_ANSWER_FAILED_MESSAGE = "К сожалению произошла ошибка, попробуйте позже";

    List<Questions> getAllQuestions();
    Questions getRandomQuestion();

}
