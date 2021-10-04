package org.mo.bots.PizzaBot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Connecting database...");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bot", "root", "&Root1399")) {
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
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
