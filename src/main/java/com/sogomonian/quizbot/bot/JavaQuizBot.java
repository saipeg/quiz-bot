package com.sogomonian.quizbot.bot;

import com.sogomonian.quizbot.config.Config;
import com.sogomonian.quizbot.model.Emojis;
import com.sogomonian.quizbot.model.KubernetesQuestions;
import com.sogomonian.quizbot.model.Questions;
import com.sogomonian.quizbot.model.User;
import com.sogomonian.quizbot.service.impl.JavaQuestionServiceImpl;
import com.sogomonian.quizbot.service.impl.KuberQuestionServiceImpl;
import com.sogomonian.quizbot.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
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

import static com.sogomonian.quizbot.model.Emojis.*;
import static com.sogomonian.quizbot.service.QuestionService.GAME_OVER;
import static com.sogomonian.quizbot.service.impl.JavaQuestionServiceImpl.TOPIC;

@Log4j2
@Component
public class JavaQuizBot extends TelegramLongPollingBot {

    private final JavaQuestionServiceImpl questionService;
    private final KuberQuestionServiceImpl kuberQuestionService;
    private final UserServiceImpl userService;
    private final Config config;

    private User user;
    private String answer;
    private Emojis emojis;

    public JavaQuizBot(JavaQuestionServiceImpl questionService, KuberQuestionServiceImpl kuberQuestionService, UserServiceImpl userService, Config config) {
        this.questionService = questionService;
        this.kuberQuestionService = kuberQuestionService;
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
        String userClick = callbackQuery.getData();

        switch (userClick) {
            case "javaQuestion":
                giveJavaQuestion(chatId);
                break;

            case "kuberQuestion":
                giveKuberQuestion(chatId);
                break;

            case "giveJavaAnswer":
                giveJavaAnswer(chatId);
                break;

            case "giveKuberAnswer":
                giveKuberAnswer(chatId);
                break;
        }
    }

    private void giveJavaAnswer(Long chatId) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Получить вопрос" + QUESTION.getCode())
                .callbackData("javaQuestion").build()));

        execute(buildMessageToSend(chatId, buttons, answer));

    }

    private void giveJavaQuestion(Long chatId) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        Questions questions = questionService.getRandomQuestionFor(chatId);
        if (questions == null) {
            String message = String.format(GAME_OVER, JavaQuestionServiceImpl.TOPIC);
            execute(buildMessageToSend(chatId, buttons, message));
            return;
        }
        answer = questions.getAnswer();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Ответ" + CHECK.getCode())
                .callbackData("giveJavaAnswer").build()));

        String message = questions.getQuestion();

        execute(buildMessageToSend(chatId, buttons, message));
    }

    private SendMessage buildMessageToSend(Long chatId, List<List<InlineKeyboardButton>> buttons, String message) {
        return SendMessage.builder()
                .text(message)
                .chatId(chatId.toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }

    private void giveKuberQuestion(Long chatId) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        KubernetesQuestions questions = kuberQuestionService.getRandomQuestionFor(chatId);

        if (questions == null) {
            String message = String.format(GAME_OVER, KuberQuestionServiceImpl.TOPIC);
            execute(buildMessageToSend(chatId, buttons, message));
            return;
        }

        answer = questions.getAnswer();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Ответ" + CHECK.getCode())
                .callbackData("giveKuberAnswer").build()));

        execute(buildMessageToSend(chatId, buttons, questions.getQuestion()));

    }

    private void giveKuberAnswer(Long chatId) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Получить вопрос" + QUESTION.getCode())
                .callbackData("kuberQuestion").build()));

        execute(buildMessageToSend(chatId, buttons, answer));

    }

    @SneakyThrows
    private void handleMessage(Message message) {
        String userName = message.getFrom().getFirstName();

        Long chatId = message.getChatId();
        boolean isChatIdNew = isChatIdNew(chatId);

        if (message.getText().equals("/start")) {
            userService.addQuestionsForClient(message.getChatId());

            List<List<InlineKeyboardButton>> buttons = getQuestionButton();
            if (isChatIdNew) {
                execute(buildMessageToSend(message.getChatId(), buttons, userName + config.getWelcome_for_new_users()));
            } else {
                execute(buildMessageToSend(message.getChatId(), buttons, userName + config.getWelcome_for_already_users()));
            }

        } else {
            String messageToUSer = ", не мямли, нажми кнопку \"Получить вопрос\" или \"Ответ\"";
            execute(SendMessage.builder().chatId(chatId.toString()).text(userName + messageToUSer).build());
        }

    }

    private boolean isChatIdNew(Long chatId) {
        boolean isUserAlreadyUsed = userService.getAllUsers().stream()
                .anyMatch(user -> user.getChatId().equals(chatId.toString()));

        if (isUserAlreadyUsed) {
            log.info("ТАКОЙ ЧАТ АЙДИ ЕСТЬ: " + chatId);
            return false;
        } else {
            log.info("ТАКОЙ ЧАТ АЙДИ НЕ НАЙДЕН: " + chatId);
            userService.addNewChatIdToBase(chatId.toString());
            return true;
        }
    }

    private List<List<InlineKeyboardButton>> getQuestionButton() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Java " + JAVA.getCode())
                .callbackData("javaQuestion").build()));

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Kubernetes " + KUBER.getCode())
                .callbackData("kuberQuestion").build()));

        return buttons;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            log.info("Register bot");
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("Error when tried register bot");
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

