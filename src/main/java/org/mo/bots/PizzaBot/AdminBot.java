package org.mo.bots.PizzaBot;

import org.mo.bots.PizzaBot.util.BotCommand;
import org.mo.bots.PizzaBot.util.CommandBot;
import org.mo.bots.PizzaBot.util.Pager;
import org.mo.bots.PizzaBot.util.Strings;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AdminBot extends CommandBot {

    private static final String redactPagerName = "redact";
    private Pager redactPager;
    private Strings strings;
    private Map<Long, String> sessions = new HashMap<>();

    public AdminBot() throws IOException {
        strings = Strings.create();
        Set<String> set = strings.getAllKeys();
        redactPager = Pager.createPager(redactPagerName, set.stream().toList(), 5, "red");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String status = sessions.getOrDefault(update.getMessage().getChatId(), "0");
            if(status.startsWith("1")) {
                finishRedact(update.getMessage(), status.substring(1), update.getMessage().getText());
            }
        }
        super.onUpdateReceived(update);
    }

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
        sendText(message, "Оберіть що змінити", redactPager.getPage(0));
    }

    private void redact(Message message, String stringName) {
        sessions.put(message.getChatId(), "1" + stringName);
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        builder.keyboardRow(Arrays.asList(InlineKeyboardButton.builder().callbackData("Back").text("Назад").build()));
        sendText(message, stringName + ":\n" + strings.get(stringName) + "\nДля редагування введіть новий текст", builder.build());
    }

    private void finishRedact(Message message, String stringName, String newValue) {
        strings.set(stringName, newValue);
        sendText(message, "Успішно відредаговано");
        sessions.remove(message.getChatId());
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
        System.out.println(data);
        if(data.startsWith("P")) {
            data = data.substring(1);
            if(data.startsWith("red")) {
                editMessage(query,"Оберіть що змінити", redactPager.getPage(Integer.parseInt(data.substring(3))));
            }
        } else if(data.startsWith("red")) {
            data = data.substring(3);
            redact(query.getMessage(), data);
        } else if(data.equals("Back")) {
            DeleteMessage deleteMessage = DeleteMessage.builder().messageId(query.getMessage().getMessageId())
                    .chatId(query.getMessage().getChatId() + "").build();
            try {
                execute(deleteMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        answerQuery(query);
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
