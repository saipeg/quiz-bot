package com.sogomonian.quizbot.model;

/**
 * @created 22/05/2022
 * @autor Andrey S
 * Some emojis for telegram-bot
 * https://symbl.cc/ru/unicode-table/#ancient-greek-numbers
 */
public enum Emojis {
    QUESTION(0x2753), CHECK(0x2705), BRAIN(0x1F393), //🎓
    CUP(0x1F3C6), //🏆
    CURSOR(0x2194), //🏆
    NEXT(0x27A1), //➡
    JAVA(0x2615),
    KUBER(0x2601)
    ;

    private final Integer code;

    Emojis(Integer code) {
        this.code = code;
    }

    public String getCode() {
        return new String(Character.toChars(code));
    }

}
