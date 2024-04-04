package ua.JavaHelper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.JavaHelper.config.BotConfig;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    static final String HELP_TEXT = "Цей бот створений для того щоб розповісти вам про Java.\n\n" +
            "Ви можете виконувати команди з головного меню зліва або ввівши команду:\n\n" +
            "Введіть /start, щоб побачити привітання\n\n" +
            "Введіть /mydata, щоб переглянути збережені дані про вас\n\n" +
            "Введіть /deletemydata, щоб видалити збережені дані про вас \n\n" +
            "Введіть /settings, щоб встановити параметри бота \n\n" +
            "Введіть /JavaHelp - переглянути список команд про Java\n\n" +
            "Введіть /help, щоб побачити це повідомлення знову \n\n";

    static final String JAVA_HELP_TEXT = "Ось список команд які стосуються Java :\n\n" +
            "Ви можете виконувати команди з головного меню зліва або ввівши команду:\n\n" +
            "Введіть /Roadmap - переглянути мапу розробника\n\n" +
            "Введіть /AboutJava - Що таке Java\n\n" +
            "Введіть /Links - переглянути корисні посилання";

    static final String ABOUT_JAVA_TEXT = "Java – це одна з найпопулярніших мов програмування. " +
            "Її розробила компанія Sun Microsystems під керівництвом Джеймса Гослінга в 1995 році. Гослінг прагнув створити інструмент," +
            " який дасть змогу розробникам писати код один раз і запускати його на будь-якій платформі без необхідності перекомпіляції (принцип WORA – Write Once and Run Anywhere). І в нього вийшло!\n" +
            "\n" +
            "Завдяки широким можливостям, бібліотекам і портативності Java дозволяє створювати ПЗ для різних компаній і сфер: ігри, мобільні застосунки, корпоративні рішення тощо. " + "\n" +
            "\n" +
            "Більше про джава можна прочитати в статті: " + "\n" + "https://goit.global/ua/articles/shcho-take-java-i-de-vona-vykorystovuietsia/";

    static final String ROADMAP_TEXT = "https://medium.com/javarevisited/the-java-programmer-roadmap-f9db163ef2c2";

    static final String LINKS_TEXT = "Сайт з більшістю основної інформації про джаву, для новачків і не тільки: " + "https://www.javatpoint.com/java-tutorial" + "\n" +
            "Сайт з переліком залежностей для maven: " + "\n" + "https://mvnrepository.com/" + "\n" +
            "Офіційний сайт Spring: " + "\n" + "https://spring.io/projects/spring-framework" + "\n" +
            "Туторіал по Java на github: " + "\n" + "https://github.com/in28minutes/java-tutorial-for-beginners" + "\n" +
            "Туторіал по Java на youtube для новачків: " + "\n" + "https://www.youtube.com/watch?v=eIrMbAQSU34&ab_channel=ProgrammingwithMosh" + "\n" +
            "Туторіал по Java на youtube для новачків (довший): " + "\n" + "https://www.youtube.com/watch?v=A74TOX803D0&ab_channel=freeCodeCamp.org";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "отримати вітальне повідомлення"));
        listOfCommands.add(new BotCommand("/help", "показати доступні команди"));
        listOfCommands.add(new BotCommand("/mydata", "отримати дані про мене"));
        listOfCommands.add(new BotCommand("/deletemydata", "видалити дані про мене"));
        listOfCommands.add(new BotCommand("/JavaHelp", "переглянути список команд про java"));
        listOfCommands.add(new BotCommand("/Roadmap", "переглянути мапу java розробника"));
        listOfCommands.add(new BotCommand("/AboutJava", "Що таке Java"));
        listOfCommands.add(new BotCommand("/Links", "переглянути корисні посилання"));
        listOfCommands.add(new BotCommand("/settings", "встановити мої параметри"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Помилка налаштування списку команд бота: {}", e.getMessage());
        }
    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;

                case "/JavaHelp":
                    sendMessage(chatId, JAVA_HELP_TEXT);
                    break;

                case "/Links":
                    sendMessage(chatId, LINKS_TEXT);
                    break;

                case "/AboutJava":
                    sendMessage(chatId, ABOUT_JAVA_TEXT);
                    break;

                case "/Roadmap":
                    sendMessage(chatId, ROADMAP_TEXT);
                    break;


                default:
                    sendMessage(chatId, "Вибачте, команда не знайдена");
                    break;
            }
        }
    }

    private void startCommandReceived(long chatId, String userName) {
        String answer = "Привіт, " + userName + " " + ", приємно познайомитись!" + "\n" + "Я твій бот-помічкник JavaHelper," +
                " який розповість тобі про Java, та поділиться цікавими матеріалами про цю мову програмування";
        log.info("Replied to user {}", userName);

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId)); //chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Сталася помилка: {}", e.getMessage());
        }

    }

}


