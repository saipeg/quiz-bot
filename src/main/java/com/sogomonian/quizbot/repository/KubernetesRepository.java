package com.sogomonian.quizbot.repository;

import com.sogomonian.quizbot.model.KubernetesQuestions;
import com.sogomonian.quizbot.model.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KubernetesRepository extends JpaRepository<KubernetesQuestions, Integer> {
}
