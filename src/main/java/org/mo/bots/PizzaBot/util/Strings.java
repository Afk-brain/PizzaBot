package org.mo.bots.PizzaBot.util;

import org.mo.bots.PizzaBot.data.MySql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Strings {

    private static Strings instance;
    private static final String sqlQuery = "SELECT * FROM string";


    public static String get(String key) {
        ResultSet result = MySql.execute("SELECT * FROM strings WHERE name='" + key + "';");
        try {
            result.next();
            return result.getNString("value");
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static List<String> getAllKeys() {
        ResultSet result = MySql.execute("SELECT name FROM strings;");
        List<String> list = new ArrayList<>();
        try {
            while (result.next()) {
                list.add(result.getNString("name"));
            }
            result.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    public static void set(String key, String value) {
        MySql.update("UPDATE strings SET value='" + value + "' WHERE name='" + key + "';");
    }


}
