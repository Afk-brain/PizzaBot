package org.mo.bots.PizzaBot.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Strings {

    private static Strings instance;

    private final Properties props = new Properties();
    private final File source = new File("src/main/resources/strings.properties");

    public static Strings create() throws IOException{
        if(instance == null) {
            return new Strings();
        } else {
            return instance;
        }
    }

    private Strings() throws IOException {
        FileReader reader = new FileReader(source);
        props.load(reader);
        reader.close();
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public boolean set(String key, String value) {
        props.setProperty(key, value);
        return save();
    }

    private boolean save() {
        try {
            FileWriter writer = new FileWriter(source);
            props.store(writer, "Saved from Strings");
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
