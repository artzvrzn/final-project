package by.itacademy.telegram.controller;


import by.itacademy.telegram.bot.FinanceTelegramBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("/")
public class TelegramController {

    private final FinanceTelegramBot bot;

    public TelegramController(FinanceTelegramBot bot) {
        this.bot = bot;
    }

    @PostMapping
    public BotApiMethod<?> onUpdate(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }

}
