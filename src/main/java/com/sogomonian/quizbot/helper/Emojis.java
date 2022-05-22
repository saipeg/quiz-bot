package com.sogomonian.quizbot.helper;

/**
 * @created 22/05/2022
 * @autor Andrey S
 * Some emojis for telegram-bot
 */
public enum Emojis {
    QUESTION(0x2753), CHECK(0x2705), BRAIN(0x1F393), //🎓
    CUP(0x1F3C6), //🏆
    CURSOR(2194) //🏆
    ;

    private Integer code;

    Emojis(Integer code) {
        this.code = code;
    }

    public String getCode() {
        return new String(Character.toChars(code));
    }

}
