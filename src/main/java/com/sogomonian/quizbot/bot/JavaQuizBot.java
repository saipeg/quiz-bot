package com.sogomonian.quizbot.bot;

import com.sogomonian.quizbot.config.Config;
import com.sogomonian.quizbot.helper.Emojis;
import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.model.User;
import com.sogomonian.quizbot.service.impl.JavaQuestionServiceImpl;
import com.sogomonian.quizbot.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sogomonian.quizbot.helper.Emojis.CHECK;
import static com.sogomonian.quizbot.helper.Emojis.QUESTION;

@Component
public class JavaQuizBot extends TelegramLongPollingBot {
    static Logger logger = LogManager.getLogger(JavaQuizBot.class);

    private final JavaQuestionServiceImpl questionService;
    private final UserServiceImpl userService;
    private final Config config;

    private User user;
    private String answer;
    private Emojis emojis;

    public JavaQuizBot(JavaQuestionServiceImpl questionService, UserServiceImpl userService, Config config) {
        this.questionService = questionService;
        this.userService = userService;
        this.config = config;
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
        Long chatId = callbackQuery.getMessage().getChatId();
        System.out.println("chat id: " + chatId); //TODO chat id to final field
        String userClick = callbackQuery.getData();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        switch (userClick) {
            case "javaQuestion":
                giveQuestion(chatId, buttons);
                break;

            case "kuberQuestion":
                giveQuestion(chatId, buttons);
                break;

            case "giveAnswer":
                giveAnswer(chatId, buttons);
                break;
        }
    }

    private void giveAnswer(Long chatId, List<List<InlineKeyboardButton>> buttons) throws TelegramApiException {
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(QUESTION.getCode() + "Получить вопрос" + QUESTION.getCode())
                .callbackData("javaQuestion").build()));

        execute(
                SendMessage.builder()
                        .text(answer)
                        .chatId(chatId.toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
    }

    private void giveQuestion(Long chatId, List<List<InlineKeyboardButton>> buttons) throws TelegramApiException {
        Questions questions = questionService.getRandomQuestion();
        answer = questions.getAnswer();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Ответ" + CHECK.getCode())
                .callbackData("giveAnswer").build()));

        execute(
                SendMessage.builder()
                        .text(questions.getQuestion())
                        .chatId(chatId.toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        String userName = message.getFrom().getFirstName();

        String chatId = message.getChatId().toString();
        checkComeback(chatId);

        if (message.getText().equals("/start")) {

            List<List<InlineKeyboardButton>> buttons = getQuestionButton();
            execute(
                    SendMessage.builder()
                            .text(userName + config.getWelcome())
                            .chatId(chatId)
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
        } else {
            execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(userName + ", не мямли, нажми кнопку \"Получить вопрос\" или \"Ответ\"")
                            .build());
        }

    }

    private void checkComeback(String chatId) {
        List<User> allUsers = userService.getAllUsers();

        allUsers.stream()
                .filter(user -> user.getChatId().equals(chatId))
                .findAny()
                .ifPresentOrElse(
                        id -> System.out.println("ТАКОЙ ЧАТ АЙДИ ЕСТЬ: " + chatId),
                        () -> {
                            System.out.println("ТАКОЙ ЧАТ АЙДИ НЕ НАЙДЕН: " + chatId);
                            userService.addNewChatIdToBase(chatId);
                        }
                );

    }

    private List<List<InlineKeyboardButton>> getQuestionButton() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(QUESTION.getCode() + "Java" + QUESTION.getCode())
                .callbackData("javaQuestion").build()));

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(QUESTION.getCode() + "Kubernetes" + QUESTION.getCode())
                .callbackData("kuberQuestion").build()));

        return buttons;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            logger.info("Register bot");
        } catch (TelegramApiException e) {
            e.printStackTrace();
            logger.error("Error when tried register bot");
        }
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    public User setUser() {
        return user;
    }
}

