package org.mo.bots.PizzaBot;

import org.mo.bots.PizzaBot.data.DataProvider;
import org.mo.bots.PizzaBot.data.PosterProvider;
import org.mo.bots.PizzaBot.objects.ClientGroup;
import org.mo.bots.PizzaBot.objects.User;
import org.mo.bots.PizzaBot.util.BotCommand;
import org.mo.bots.PizzaBot.util.CommandBot;
import org.mo.bots.PizzaBot.util.Pager;
import org.mo.bots.PizzaBot.util.Strings;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
import java.util.*;

public class AdminBot extends CommandBot {

    private static final String redactPagerName = "redact";
    private static final String clientsGroupPagerName = "client";
    private Pager redactPager;
    private Pager clientsGroupPager;
    private Map<Long, String> sessions = new HashMap<>();
    private DataProvider dataProvider = new PosterProvider();
    private PizzaBot sender;

    public AdminBot(PizzaBot sender) throws IOException {
        this.sender = sender;
        redactPager = Pager.createPager(redactPagerName, Strings.getAllKeys(), 5, "red", null);
        List<String> groups = new ArrayList<>();
        for(ClientGroup group : dataProvider.getClientGroups()) {
            groups.add(group.client_groups_name + " " + group.client_groups_id);
        }
        clientsGroupPager = Pager.createPager(clientsGroupPagerName, groups, 5, "clig", null);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String status = sessions.getOrDefault(update.getMessage().getChatId(), "0");
            if(status.startsWith("1")) {
                finishRedact(update.getMessage(), status.substring(1), update.getMessage().getText());
            } else if(status.startsWith("2")) {
                finishMail(update.getMessage(), status.substring(1));
            }
        }
        super.onUpdateReceived(update);
    }

    @BotCommand("/start")
    public void start(Message message) {
        ReplyKeyboardMarkup keyboard = ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboardRow(createKeyboardRow("???????????????????? ??????????????????????\uD83D\uDCDD"))
                .keyboardRow(createKeyboardRow("????????????????\uD83D\uDCE7"))
                .build();
        sendText(message, "???????????????? ??????????", keyboard);
    }

    @BotCommand("???????????????????? ??????????????????????\uD83D\uDCDD")
    public void changeText(Message message) {
        sendText(message, "?????????????? ???? ??????????????", redactPager.getPage(0));
    }

    @BotCommand("????????????????\uD83D\uDCE7")
    public void mail(Message message) {
        sendText(message, "?????????????? ?????????? ????????????????", clientsGroupPager.getPage(0));
    }

    private void redact(Message message, String stringName) {
        sessions.put(message.getChatId(), "1" + stringName);
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        builder.keyboardRow(Arrays.asList(InlineKeyboardButton.builder().callbackData("Back1").text("??????????").build()));
        editMessage(message,stringName + ":\n" + Strings.get(stringName) + "\n?????? ?????????????????????? ?????????????? ?????????? ??????????", builder.build());
        //sendText(message, stringName + ":\n" + strings.get(stringName) + "\n?????? ?????????????????????? ?????????????? ?????????? ??????????", builder.build());
    }

    private void mailTo(Message message, String group) {
        sessions.put(message.getChatId(), "2" + group);
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        builder.keyboardRow(Arrays.asList(InlineKeyboardButton.builder().callbackData("Back2").text("??????????").build()));
        editMessage(message, "?????????????? ?????????? ?????? ???????????????? ?????????? " + group, builder.build());
    }

    private void finishRedact(Message message, String stringName, String newValue) {
        Strings.set(stringName, newValue);
        sendText(message, "?????????????? ??????????????????????????");
        sessions.remove(message.getChatId());
    }

    private void finishMail(Message message, String group) {
        String[] parts = group.split(" ");
        String idString = parts[parts.length - 1];
        long id = Long.parseLong(idString);
        List<User> users = dataProvider.getUsersInGroup(id);
        for(User user : users) {
            if(user.chatId.isBlank()) continue;
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(user.chatId + "")
                    .text(message.getText())
                    .build();
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException exception) {
                exception.printStackTrace();
            }
        }
        sendText(message, "?????????????? ??????????????????");
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
                editMessage(query.getMessage(),"?????????????? ???? ??????????????", redactPager.getPage(Integer.parseInt(data.substring(3))));
            } else if(data.startsWith("clig")) {
                editMessage(query.getMessage(),"?????????????? ?????????? ????????????????", redactPager.getPage(Integer.parseInt(data.substring(3))));
            }
        } else if(data.startsWith("red")) {
            data = data.substring(3);
            redact(query.getMessage(), data);
        } else if(data.startsWith("clig")) {
            data = data.substring(4);
            mailTo(query.getMessage(), data);
        } else if(data.equals("Back1")) {
            editMessage(query.getMessage(), "?????????????? ???? ??????????????", redactPager.getPage(0));
            sessions.remove(query.getMessage().getChatId());
        } else if(data.equals("Back2")) {
            sendText(query.getMessage(), "?????????????? ?????????? ????????????????", clientsGroupPager.getPage(0));
            sessions.remove(query.getMessage().getChatId());
        }
        answerQuery(query, "");
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
