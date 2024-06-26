package com.sogomonian.quizbot.service;

import com.sogomonian.quizbot.model.Questions;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface QuestionService {

    List<Questions> getAllQuestions();
    Questions randomQuestion();

}
