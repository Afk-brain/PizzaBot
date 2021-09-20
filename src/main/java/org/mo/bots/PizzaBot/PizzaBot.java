package org.mo.bots.PizzaBot;

import org.mo.bots.PizzaBot.util.CommandBot;

public class PizzaBot extends CommandBot {

    @Override
    public String getBotUsername() {
        return System.getenv("BotUsername");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BotToken");
    }
}
