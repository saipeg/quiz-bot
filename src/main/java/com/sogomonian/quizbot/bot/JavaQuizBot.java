package com.sogomonian.quizbot.bot;

import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.service.impl.QuestionServiceImpl;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class JavaQuizBot extends TelegramLongPollingBot {

    String answer;

    static Logger logger = LogManager.getLogger(JavaQuizBot.class);
    private final QuestionServiceImpl questionService;

    @Value("${bot.token}")
    private String token;
    @Value("${bot.name}")
    private String username;

    public JavaQuizBot(QuestionServiceImpl questionService) {
        this.questionService = questionService;
    }


    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            logger.info("Register bot");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    @SneakyThrows
    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String data = callbackQuery.getData();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        if (data.equals("giveQuestion")) {
            Questions questions = questionService.randomQuestion();
            answer = questions.getAnswer();
            execute(SendMessage.builder()
                    .text(questions.getQuestion())
                    .chatId(message.getChatId().toString())
                    .build());
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text("Проверить")
                                    .callbackData("giveAnswer")
                                    .build()
                    )
            );
        }

        if (data.equals("giveAnswer")) {
            execute(SendMessage.builder()
                    .text(answer)
                    .chatId(message.getChatId().toString())
                    .build());

            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text("Получить вопрос")
                                    .callbackData("giveQuestion")
                                    .build()
                    )
            );

            getQuestionButton();
            execute(SendMessage.builder()
                    .text("\n ---------------------------------")
                    .chatId(message.getChatId().toString())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build());

//            handleMessage(callbackQuery.getMessage());
        }

        execute(EditMessageReplyMarkup.builder()
                        .chatId(message.getChatId().toString())
                        .messageId(message.getMessageId())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
    }

    @SneakyThrows
    private void handleMessage(Message message) {
//        if (message.hasText()) {
//        }
        List<List<InlineKeyboardButton>> buttons = getQuestionButton();

        execute(SendMessage.builder()
                .text("Привет! Ты пишешь боту, который поможет освежить основные теоретические вопросы по java-core!")
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private List<List<InlineKeyboardButton>> getQuestionButton() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(
                Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text("Получить вопрос")
                                .callbackData("giveQuestion")
                                .build()
                )
        );
        return buttons;
    }

//    private InlineKeyboardMarkup sendInlineKeyBoardMessage() {
//        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//
//        InlineKeyboardButton getQuestion = new InlineKeyboardButton();
//
//        getQuestion.setText("Получить вопрос");
//        getQuestion.setCallbackData("buttonYes");
//
//        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
//
//        keyboardButtonsRow1.add(getQuestion);
//
//        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow1));
//
////        return new SendMessage().setChatId(chatId).setText("Пример").setReplyMarkup(inlineKeyboardMarkup);
//        return inlineKeyboardMarkup;
//    }


    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}

