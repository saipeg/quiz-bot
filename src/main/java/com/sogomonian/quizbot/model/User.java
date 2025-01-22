package com.sogomonian.quizbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(schema = "public",name = "user")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "chatid")
    private String chatId;

    @Column(name = "last_time")
    private LocalDateTime lastTime;

    @Override
    public String toString() {
        return chatId
                + "\n"
                + "\n"
                + lastTime;
    }
}
