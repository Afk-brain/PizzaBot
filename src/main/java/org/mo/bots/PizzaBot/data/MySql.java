package org.mo.bots.PizzaBot.data;

import java.sql.*;

public class MySql {

    static Connection connection;
    static Statement statement;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bot", "root", "&Root1399");
            statement = connection.createStatement();
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public static ResultSet execute(String SQL) {
        try {
            return statement.executeQuery(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int update(String SQL) {
        try {
            return statement.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getTgId(String phone) {
        ResultSet result = execute("SELECT *  FROM tgids WHERE phone='" + phone + "'");
        try {
            if(!result.next()) return "";
            String id = result.getString("id");
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private MySql() {}

}
