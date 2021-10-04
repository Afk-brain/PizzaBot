package org.mo.bots.PizzaBot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Pager {

    private static Map<String, Pager> pagers = new HashMap<>();

    private int pageCup = 5;
    private int pages;
    private List<String> data;
    private String dataPrefix;
    private Function<String, String> dataFunction;

    public static boolean pagerExists(String name) {
        return pagers.containsKey(name);
    }

    public static Pager getPager(String name) {
        return pagers.get(name);
    }

    public static Pager createPager(String name, List<String> data, int pageCup, String dataPrefix, Function<String, String> dataFunction) {
        if(pagerExists(name)) {
            throw new IllegalArgumentException();
        }
        Pager pager = new Pager();
        pager.data = data;
        pager.pageCup = pageCup;
        pager.dataPrefix = dataPrefix;
        pager.dataFunction = dataFunction;
        pager.calcPages();
        pagers.put(name, pager);
        return pager;
    }

    public void setData(List<String> data) {
        this.data = data;
        calcPages();
    }

    private void setPageCup(int pageCup) {
        this.pageCup = pageCup;
        calcPages();
    }

    private void setDataPrefix(String dataPrefix) {
        this.dataPrefix = dataPrefix;
    }

    public InlineKeyboardMarkup getPage(int page) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder builder = InlineKeyboardMarkup.builder();
        if(page < 0) {
            page = 0;
        } else if(page >= pages) {
            page = pages - 1;
        }
        for(int i = page * pageCup;i < Math.min(data.size(), (page + 1) * pageCup);i++) {
            String dataString = data.get(i);
            if(dataFunction != null) {
                dataString = dataFunction.apply(dataString);
            }
            builder.keyboardRow(Arrays.asList(createButton(dataPrefix + dataString, data.get(i))));
        }
        if(pages != 1) {
            builder.keyboardRow(Arrays.asList(createButton("P" + dataPrefix + (page - 1), "<<"),
                    createButton("pages", (page + 1) + "/" + pages), createButton("P" + dataPrefix + (page + 1), ">>")));
        }
        return builder.build();
    }

    private InlineKeyboardButton createButton(String data, String text) {
        return InlineKeyboardButton.builder().text(text).callbackData(data).build();
    }

    private void calcPages() {
        pages = (int) Math.ceil(data.size() / (float)pageCup);
    }

    private Pager() {}

}
