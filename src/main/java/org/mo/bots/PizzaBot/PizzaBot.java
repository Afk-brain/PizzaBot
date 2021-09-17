package org.mo.bots.PizzaBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PizzaBot extends TelegramLongPollingBot {

    private Logger log = LoggerFactory.getLogger(PizzaBot.class);

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            SendMessage sendMessage = SendMessage.builder()
                    .replyToMessageId(update.getMessage().getMessageId())
                    .text(update.getMessage().getText())
                    .chatId(update.getMessage().getChatId() + "")
                    .build();
            try {
                execute(sendMessage);
            } catch (Exception e) {
                log.error("EXECUTION FAILED");
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return System.getenv("BotUsername");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BotToken");
    }
}
