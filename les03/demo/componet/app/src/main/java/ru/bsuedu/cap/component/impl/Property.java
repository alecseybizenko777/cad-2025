package ru.bsuedu.cap.component.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component("property")
public class Property {
    private final String version = "1.0.0";

    public String getVersion() {
        return version;
    }

    private final String name = "Component";

    public String getName() {
        return name;
    }

    private final String author = "Sergey";

    public String getAuthor() {
        return author;
    }

    private final List<String> numList = Arrays.asList("1", "2", "3", "4", "5");

    public List<String> getNumList() {
        return numList;
    }

    public String getFileName() {
        Properties prop = new Properties();
        try {
            prop.load(Property.class.getClassLoader().getResourceAsStream("application.properties"));
            return prop.getProperty("filename");

        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
