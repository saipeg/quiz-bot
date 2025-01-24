package com.sogomonian.quizbot.service;

import java.util.List;

public interface QuestionService<Q> {
    String REPOSITORY_ANSWER_FAILED_MESSAGE = "К сожалению произошла ошибка, попробуйте позже";

    List<Q> getAllQuestions();

    Q getRandomQuestionFor(Long chatId);

}
