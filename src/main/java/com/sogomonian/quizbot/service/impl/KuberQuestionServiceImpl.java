package com.sogomonian.quizbot.service.impl;

import com.sogomonian.quizbot.model.KubernetesQuestions;
import com.sogomonian.quizbot.repository.KubernetesRepository;
import com.sogomonian.quizbot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KuberQuestionServiceImpl implements QuestionService<KubernetesQuestions> {

    private final KubernetesRepository kubernetesRepository;
    private List<KubernetesQuestions> allQuestions;
    private UserServiceImpl userService;

    @Autowired
    public KuberQuestionServiceImpl(KubernetesRepository kubernetesRepository, List<KubernetesQuestions> allQuestions, @Lazy UserServiceImpl userService) {
        this.kubernetesRepository = kubernetesRepository;
        this.allQuestions = allQuestions;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        allQuestions = getAllQuestions();
    }

    public KubernetesQuestions getRandomQuestionFor(Long chatId) {

        Map<String, Integer> topicsAndLastQuestions = userService.getVacantQuestions().get(chatId);
        Integer lastQuestions = topicsAndLastQuestions.get("kuber");


        if (lastQuestions > 0) {
            val question = allQuestions.get(lastQuestions - 1);
            lastQuestions -= 1;
            Map<String, Integer> questions = new HashMap<>();
            questions.put("kuber", lastQuestions);
            userService.getVacantQuestions().put(chatId, questions);
            return question;
        } else {
            System.out.println("      ВОПРОСЫ ЗАКОНЧМИЛИСЬ    ");
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
