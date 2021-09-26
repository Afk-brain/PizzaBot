package org.mo.bots.PizzaBot.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class Strings {

    private static Strings instance;

    private static final Properties props = new Properties();
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
        key = key.replaceAll(" ", "_");
        return props.getProperty(key).replaceAll("_", " ");
    }

    public Set<String> getAllKeys() {
        return props.stringPropertyNames();
    }

    public boolean set(String key, String value) {
        key = key.replaceAll(" ", "_");
        value = value.replaceAll(" ", "_");
        if(props.containsKey(key)) {
            props.replace(key, value);
        } else {
            props.setProperty(key, value);
        }
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
