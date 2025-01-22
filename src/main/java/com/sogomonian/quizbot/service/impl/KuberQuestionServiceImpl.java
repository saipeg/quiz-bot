package com.sogomonian.quizbot.service.impl;

import com.sogomonian.quizbot.model.KubernetesQuestions;
import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.repository.KubernetesRepository;
import com.sogomonian.quizbot.service.QuestionService;
import com.sogomonian.quizbot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class KuberQuestionServiceImpl implements QuestionService{

    private static final Logger LOGGER = LogManager.getLogger(KuberQuestionServiceImpl.class);

    private KubernetesRepository kubernetesRepository;
    private List<Questions> allQuestions;


    public List<Questions> getAllQuestions() {
        return null;
    }

    public KubernetesQuestions getRandomQuestion() {

        allQuestions = getAllKubernetosQuestions();

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

    public List<Questions> getAllKubernetosQuestions() {
        List<Questions> kubernetesQuestions;
        try {
            kubernetesQuestions = kubernetesRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Repository not found");
            return new ArrayList<>();
        }

        return kubernetesQuestions;
    }
}
