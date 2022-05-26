package com.sogomonian.quizbot.bot;

import com.sogomonian.quizbot.helper.Emojis;
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

import static com.sogomonian.quizbot.helper.Emojis.*;

@Component
public class JavaQuizBot extends TelegramLongPollingBot {

    String answer;

    static Logger logger = LogManager.getLogger(JavaQuizBot.class);
    private final QuestionServiceImpl questionService;
    private Emojis emojis;

    @Value("${bot.token}")
    private String token;
    @Value("${bot.name}")
    private String username;
    @Value("${bot.welcome}")
    private String welcome;

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
            buttons.add(Arrays.asList(InlineKeyboardButton.builder().text("Ответ" + CHECK.getCode()).callbackData("giveAnswer").build()));

            execute(SendMessage.builder().text(questions.getQuestion()).chatId(message.getChatId().toString()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build()).build());
//          buttons.add(Arrays.asList(InlineKeyboardButton.builder().text("Проверить" + CHECK.getCode()).callbackData("giveAnswer").build()));
//            buttons.add(Arrays.asList(InlineKeyboardButton.builder().text("Ответ" + CHECK.getCode()).callbackData("giveAnswer").build()));
//            execute(EditMessageReplyMarkup.builder().chatId(message.getChatId().toString()).messageId(message.getMessageId()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build()).build());
//            execute(SendMessage.builder().text("Проверить" + CHECK.getCode()).chatId(message.getChatId().toString()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build()).build());

        }

        if (data.equals("giveAnswer")) {
            buttons.add(Arrays.asList(InlineKeyboardButton.builder().text(QUESTION.getCode() + "Получить вопрос" + QUESTION.getCode()).callbackData("giveQuestion").build()));

            execute(SendMessage.builder().text(answer).chatId(message.getChatId().toString()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build()).build());
//            buttons.add(Arrays.asList(InlineKeyboardButton.builder().text(QUESTION.getCode() + "Получить вопрос" + QUESTION.getCode()).callbackData("giveQuestion").build()));
//            execute(SendMessage.builder().text("Попробуем еще?" + NEXT.getCode()).chatId(message.getChatId().toString()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build()).build());

        }
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        if (message.getText().equals("/start")) {

            List<List<InlineKeyboardButton>> buttons = getQuestionButton();
            execute(SendMessage.builder().text(welcome).chatId(message.getChatId().toString()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build()).build());
        } else {
            execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Не мямли, нажми кнопку \"Получить вопрос\" или \"Ответ\"").build());
        }

    }

    private List<List<InlineKeyboardButton>> getQuestionButton() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(InlineKeyboardButton.builder().text(QUESTION.getCode() + "Получить вопрос" + QUESTION.getCode()).callbackData("giveQuestion").build()));
        return buttons;
    }


    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}

