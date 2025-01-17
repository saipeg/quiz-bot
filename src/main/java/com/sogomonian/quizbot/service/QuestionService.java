package com.sogomonian.quizbot.service;

import com.sogomonian.quizbot.model.Questions;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface QuestionService {
    String REPOSITORY_ANSWER_FAILED_MESSAGE = "К сожалению произошла ошибка, попробуйте позже";

    List<Questions> getAllQuestions();
    Questions randomQuestion();

}
