package org.mo.bots.PizzaBot;

import org.mo.bots.PizzaBot.data.MySql;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            PizzaBot pizzaBot = new PizzaBot();
            telegramBotsApi.registerBot(pizzaBot);
            telegramBotsApi.registerBot(new AdminBot(pizzaBot));
            System.out.println("BOTS INITIALIZED");
        } catch (Exception e) {
            System.out.println("BOTS INITIALIZATION FAILED");
            e.printStackTrace();
        }
    }

}
