package org.mo.bots.PizzaBot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new PizzaBot());
            System.out.println("BOT INITIALIZED");
        } catch (Exception e) {
            System.out.println("BOT INITIALIZATION FAILED");
            e.printStackTrace();
        }
    }

}
