package org.muratcan.petvaccine.reminder.bot;

import org.muratcan.petvaccine.reminder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class VaccineReminderBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(VaccineReminderBot.class);
    private final String botUsername;
    private final UserService userService;

    public VaccineReminderBot(@Value("${telegram.bot.username}") String botUsername,
                              @Value("${telegram.bot.token}") String botToken,
                              DefaultBotOptions options,
                              UserService userService) {
        super(options, botToken);
        this.botUsername = botUsername;
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();
            String username = String.format("%s %s", update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getLastName());

            if (messageText.equalsIgnoreCase("/start")) {
                String welcomeMessage = """
                        Hello! Welcome to the Pet Vaccine Reminder Bot. Send "/notification your_email_address" to get notifications for your pet vaccine reminders.
                        """;
                sendMessage(chatId, welcomeMessage);
            } else if (messageText.startsWith("/notification")) {
                String email = messageText.replace("/notification", "").trim();
                String notificationSuccessMessage = String
                        .format("Hello, %s! Welcome to the Pet Vaccine Reminder Bot. You will now receive reminders for your pet's vaccinations.", username);
                boolean result = userService.saveChatIdForUser(email, chatId);
                if (result) {
                    sendMessage(chatId, notificationSuccessMessage);
                } else {
                    String notificationErrormessage = String
                            .format("Hello, %s! Welcome to the Pet Vaccine Reminder Bot. Please make sure your email is correct.", username);
                    sendMessage(chatId, notificationErrormessage);
                }
            }
        }
    }

    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message to chatId: {} with error: {}", chatId, e.getMessage());
        }
    }
}



