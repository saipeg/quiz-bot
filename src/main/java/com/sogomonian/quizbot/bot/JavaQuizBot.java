package com.sogomonian.quizbot.bot;

import com.sogomonian.quizbot.config.Config;
import com.sogomonian.quizbot.helper.Emojis;
import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.service.impl.QuestionServiceImpl;
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

import static com.sogomonian.quizbot.helper.Emojis.CHECK;
import static com.sogomonian.quizbot.helper.Emojis.QUESTION;

@Component
public class JavaQuizBot extends TelegramLongPollingBot {
    static Logger logger = LogManager.getLogger(JavaQuizBot.class);

    private final QuestionServiceImpl questionService;
    private final Config config;

    private String answer;
    private Emojis emojis;

    public JavaQuizBot(QuestionServiceImpl questionService, Config config) {
        this.questionService = questionService;
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
        Message message = callbackQuery.getMessage();
        String userClick = callbackQuery.getData();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        if (userClick.equals("giveQuestion")) {

            Questions questions = questionService.randomQuestion();
            answer = questions.getAnswer();

            buttons.add(List.of(InlineKeyboardButton.builder()
                    .text("Ответ" + CHECK.getCode())
                    .callbackData("giveAnswer").build()));

            execute(
                    SendMessage.builder()
                            .text(questions.getQuestion())
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());

        }

        if (userClick.equals("giveAnswer")) {

            buttons.add(List.of(InlineKeyboardButton.builder()
                    .text(QUESTION.getCode() + "Получить вопрос" + QUESTION.getCode())
                    .callbackData("giveQuestion").build()));

            execute(
                    SendMessage.builder()
                            .text(answer)
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());

        }
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        String userName = message.getFrom().getFirstName();

        if (message.getText().equals("/start")) {

            List<List<InlineKeyboardButton>> buttons = getQuestionButton();
            execute(
                    SendMessage.builder()
                            .text(userName + config.getWelcome())
                            .chatId(message.getChatId().toString())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build());
        } else {
            execute(
                    SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text(userName + ", не мямли, нажми кнопку \"Получить вопрос\" или \"Ответ\"")
                            .build());
        }

    }

    private List<List<InlineKeyboardButton>> getQuestionButton() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(QUESTION.getCode() + "Получить вопрос" + QUESTION.getCode())
                .callbackData("giveQuestion").build()));

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
}

