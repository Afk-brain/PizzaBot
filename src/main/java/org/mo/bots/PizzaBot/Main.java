package org.mo.bots.PizzaBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new PizzaBot());
            log.info("BOT INITIALIZED");
        } catch (Exception e) {
            log.error("BOT INITIALIZATION FAILED");
            e.printStackTrace();
        }
    }

}
