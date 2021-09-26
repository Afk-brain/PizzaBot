package org.mo.bots.PizzaBot;

import org.mo.bots.PizzaBot.util.BotCommand;
import org.mo.bots.PizzaBot.util.CommandBot;
import org.mo.bots.PizzaBot.util.Strings;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;

public class PizzaBot extends CommandBot {

    private Strings strings = Strings.create();

    public PizzaBot() throws IOException {
    }

    @BotCommand("/start")
    public void start(Message message) {
        ReplyKeyboardMarkup keyboard = ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboardRow(createKeyboardRow("\uD83D\uDC3CРекламка\uD83D\uDC3C"))
                .keyboardRow(createKeyboardRow("\uD83D\uDC3EНапої\uD83C\uDF7A", "\uD83D\uDC3EПіцца\uD83C\uDF55"))
                .keyboardRow(createKeyboardRow("\uD83D\uDC3EКошик\uD83D\uDCE6", "\uD83D\uDC3EРезервування\uD83D\uDECB"))
                .build();
        sendText(message, strings.get("Привітання"), keyboard);
    }

    @BotCommand("\uD83D\uDC3EКошик\uD83D\uDCE6")
    public void bracket(Message message) {
        sendText(message, strings.get("Порожній кошик"));
    }

    @BotCommand("\uD83D\uDC3CРекламка\uD83D\uDC3C")
    public void promo(Message message) {
        sendText(message, strings.get("Реклама"));
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
        return System.getenv("BotUsername");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BotToken");
    }
}
