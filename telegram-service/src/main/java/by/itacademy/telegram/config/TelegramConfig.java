package by.itacademy.telegram.config;

import by.itacademy.telegram.bot.FinanceTelegramBot;
import by.itacademy.telegram.view.handler.MessageHandlers;
import by.itacademy.telegram.view.handler.api.CallBackQueryHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class TelegramConfig {


    @Value("${telegram.webhook-path}")
    private String webhookPath;
    @Value("${telegram.bot-name}")
    private String botName;
    @Value("${telegram.bot-token}")
    private String botToken;


    @Bean
    public SetWebhook setWebhook() {
        return SetWebhook.builder().url(webhookPath).build();
    }

    @Bean
    public FinanceTelegramBot financeTelegramBot(SetWebhook setWebhook,
                                                 MessageHandlers messageHandlers,
                                                 CallBackQueryHandler callBackQueryHandler) {
        FinanceTelegramBot financeBot = new FinanceTelegramBot(setWebhook, messageHandlers, callBackQueryHandler);
        financeBot.setBotPath(webhookPath);
        financeBot.setBotName(botName);
        financeBot.setBotToken(botToken);
        return financeBot;
    }
}
