package org.mo.bots.PizzaBot;

import org.mo.bots.PizzaBot.util.BotCommand;
import org.mo.bots.PizzaBot.util.CommandBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class AdminBot extends CommandBot {

    @BotCommand("/start")
    public void start(Message message) {
        ReplyKeyboardMarkup keyboard = ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboardRow(createKeyboardRow("Редагуваня повідомлень\uD83D\uDCDD"))
                .build();
        sendText(message, "Виберіть опцію", keyboard);
    }

    @BotCommand("Редагуваня повідомлень\uD83D\uDCDD")
    public void changeText(Message message) {

    }

    private KeyboardRow createKeyboardRow(String... buttons) {
        KeyboardRow row = new KeyboardRow();
        for(String button : buttons) {
            row.add(button);
        }
        return row;
    }

    @Override
    protected void processCallbackQuery(CallbackQuery query) {

    }

    @Override
    public String getBotUsername() {
        return System.getenv("AdminBotUsername");
    }

    @Override
    public String getBotToken() {
        return System.getenv("AdminBotToken");
    }
}
