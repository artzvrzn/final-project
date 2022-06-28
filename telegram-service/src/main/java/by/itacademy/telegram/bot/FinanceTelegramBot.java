package by.itacademy.telegram.bot;

import by.itacademy.telegram.view.handler.MessageHandlers;
import by.itacademy.telegram.view.handler.api.CallBackQueryHandler;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import static by.itacademy.telegram.model.constant.Reply.EXCEPTION;

@Log4j2
public class FinanceTelegramBot extends SpringWebhookBot {

    private final MessageHandlers messageHandlers;
    private final CallBackQueryHandler callbackQueryHandler;
    private String botPath;
    private String botName;
    private String botToken;


    public FinanceTelegramBot(SetWebhook setWebhook,
                              MessageHandlers messageHandlers,
                              CallBackQueryHandler callbackQueryHandler) {
        super(setWebhook);
        this.messageHandlers = messageHandlers;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotPath(String webhookPath) {
        this.botPath = webhookPath;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (Exception exc) {
            log.error(exc.getMessage());
            return new SendMessage(update.getMessage().getChatId().toString(), EXCEPTION.getText());
        }
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Bot has failed to send message: {}", e.getMessage());
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.handle(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message != null) {
                return messageHandlers.handle(message);
            }
        }
        return null;
    }
}
