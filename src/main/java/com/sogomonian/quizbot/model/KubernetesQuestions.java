package com.sogomonian.quizbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(schema = "public",name = "kubernetes_questions")
public class KubernetesQuestions extends Questions {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "question")
    private String question;

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Column(name = "answer")
    private String answer;

    @Override
    public String toString() {
        return question
                + "\n"
                + "\n"
                + answer;
    }
}
