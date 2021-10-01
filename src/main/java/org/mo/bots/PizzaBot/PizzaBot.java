package org.mo.bots.PizzaBot;

import org.mo.bots.PizzaBot.data.DataProvider;
import org.mo.bots.PizzaBot.data.PosterProvider;
import org.mo.bots.PizzaBot.util.BotCommand;
import org.mo.bots.PizzaBot.util.CommandBot;
import org.mo.bots.PizzaBot.util.Strings;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Arrays;

public class PizzaBot extends CommandBot {

    private Strings strings = Strings.create();
    private DataProvider dataProvider = new PosterProvider();

    public PizzaBot() throws IOException {
    }

    @BotCommand("/start")
    public void start(Message message) {
        ReplyKeyboardMarkup keyboard = ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboardRow(createKeyboardRow("\uD83D\uDC3CРекламка\uD83D\uDC3C"))
                .keyboardRow(createKeyboardRow("\uD83D\uDC3EНапої\uD83C\uDF7A", "\uD83D\uDC3EПіцца і закуски\uD83C\uDF55"))
                .keyboardRow(createKeyboardRow("\uD83D\uDC3EКошик\uD83D\uDCE6", "\uD83D\uDC3EРезервування\uD83D\uDECB"))
                .build();
        sendText(message, strings.get("Привітання"), keyboard);
    }

    @BotCommand("\uD83D\uDC3EНапої\uD83C\uDF7A")
    public void drinks(Message message) {
        showProduct1(message);
    }

    @BotCommand("\uD83D\uDC3EКошик\uD83D\uDCE6")
    public void bracket(Message message) {
        sendText(message, strings.get("Порожній кошик"));
    }

    @BotCommand("\uD83D\uDC3CРекламка\uD83D\uDC3C")
    public void promo(Message message) {
        sendText(message, strings.get("Реклама"));
        dataProvider.getClientGroups();
    }

    public void showProduct1(Message message) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboardRow(Arrays.asList(InlineKeyboardButton.builder().callbackData("Alert").text("Додати в кошик").build()))
                .keyboardRow(Arrays.asList(InlineKeyboardButton.builder().callbackData("Back").text("Назад").build()))
                .build();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("*Назва товару*\n" +
                        "\nІнформація про товар\n" +
                        "\nХарактеристики товару\n" +
                        "\nЦіна: 30\\.00₴" +
                        "[\u200E](https://ochakovo.ru/wp-content/uploads/2020/10/0-3.png)")
                .parseMode("MarkdownV2")
                .replyMarkup(markup)
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
        String data = query.getData();
        if(data.equals("Back")) {
            DeleteMessage deleteMessage = DeleteMessage.builder().messageId(query.getMessage().getMessageId())
                    .chatId(query.getMessage().getChatId() + "").build();
            try {
                execute(deleteMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if(data.equals("Alert")) {
            answerQuery(query, "Товар додано в кошик");
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
