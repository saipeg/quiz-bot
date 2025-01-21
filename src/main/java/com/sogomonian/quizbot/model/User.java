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
@Table(schema = "public",name = "user")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "chatId")
    private String chatId;

    @Column(name = "last_time")
    private String lastTime;

    @Override
    public String toString() {
        return chatId
                + "\n"
                + "\n"
                + lastTime;
    }
}
