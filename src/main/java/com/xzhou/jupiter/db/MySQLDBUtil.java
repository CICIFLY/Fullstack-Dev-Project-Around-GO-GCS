package com.xzhou.jupiter.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class MySQLDBUtil {
    private static final String INSTANCE = "laiproject-database.cnsmu7t4x8vx.us-east-2.rds.amazonaws.com";
    private static final String PORT_NUM = "3306";
    private static final String DB_NAME = "jupiter";
    public static String getMySQLAddress() throws IOException {
        Properties prop = new Properties();
        String propFileName = "config.properties";


        InputStream inputStream = MySQLDBUtil.class.getClassLoader().getResourceAsStream(propFileName); //relative path .  find path
        prop.load(inputStream);  // load data


        String username = prop.getProperty("user");
        String password = prop.getProperty("password");
        return String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s&autoReconnect=true&serverTimezone=UTC&createDatabaseIfNotExist=true",
                INSTANCE, PORT_NUM, DB_NAME, username, password);
    }

}
