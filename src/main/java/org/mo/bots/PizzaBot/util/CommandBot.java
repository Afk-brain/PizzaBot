package org.mo.bots.PizzaBot.util;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandBot extends TelegramLongPollingBot {

    private final Map<String, Method> methods;

    public CommandBot() {
        methods = new HashMap<>();
        Class<?> botClass = getClass();
        for(Method method : botClass.getMethods()) {
            for(Annotation annotation : method.getDeclaredAnnotations()) {
                if(annotation instanceof BotCommand) {
                    methods.put(((BotCommand) annotation).value(), method);
                }
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        if(update.hasMessage() && update.getMessage().hasText()) {
            String input = update.getMessage().getText();
            methods.forEach((string, method) -> {
                if(input.equals(string)) {
                    try {
                        method.invoke(this,update.getMessage());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
        } else if(update.hasCallbackQuery()) {
            processCallbackQuery(update.getCallbackQuery());
        }
    }

    protected abstract void processCallbackQuery(CallbackQuery query);


    protected void sendText(Message message, String text) {
        sendText(message, text, null);
    }

    protected void sendText(Message message, String text, ReplyKeyboard keyboard) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(text)
                .replyMarkup(keyboard)
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException exception) {
            exception.printStackTrace();
        }
    }

    protected void editMessage(Message message, String text, InlineKeyboardMarkup keyboard) {
        EditMessageText edit = EditMessageText.builder()
                .messageId(message.getMessageId())
                .chatId(message.getChatId().toString())
                .replyMarkup(keyboard)
                .text(text)
                .build();
        try {
            execute(edit);
        } catch (TelegramApiException exception) {
            exception.printStackTrace();
        }
    }

    protected void answerQuery(CallbackQuery query, String text) {
        AnswerCallbackQuery answer = AnswerCallbackQuery.builder().text(text).callbackQueryId(query.getId()).build();
        try {
            execute(answer);
        } catch (TelegramApiException exception) {
            exception.printStackTrace();
        }
    }

}
