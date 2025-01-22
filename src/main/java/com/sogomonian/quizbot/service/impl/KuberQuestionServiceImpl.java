package com.sogomonian.quizbot.service.impl;

import com.sogomonian.quizbot.model.KubernetesQuestions;
import com.sogomonian.quizbot.repository.KubernetesRepository;
import com.sogomonian.quizbot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class KuberQuestionServiceImpl implements QuestionService<KubernetesQuestions> {

    private final KubernetesRepository kubernetesRepository;
    private List<KubernetesQuestions> allQuestions;

    @PostConstruct
    public void init() {
        allQuestions = getAllQuestions();
    }

    public KubernetesQuestions getRandomQuestion() {
        if (!allQuestions.isEmpty()) {
            System.out.println("=========" + allQuestions.size());
            Random r = new Random();
            int questionId = r.nextInt(allQuestions.size());
            val question = allQuestions.get(questionId);
//            allQuestions.remove(questionId);
//            return new Response(question, questionId);
            return question;
        } else {
            return null;
        }
    }

    public List<KubernetesQuestions> getAllQuestions() {
        List<KubernetesQuestions> kubernetesQuestions;
        try {
            kubernetesQuestions = kubernetesRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Repository not found");
            return Collections.emptyList();
        }
        return kubernetesQuestions;
    }

    static class Response {
        public Response(KubernetesQuestions kubernetesQuestions, Integer id) {
            this.kubernetesQuestions = kubernetesQuestions;
            this.id = id;
        }

        KubernetesQuestions kubernetesQuestions;
        Integer id;
    }
}
