package com.crock.proxy.db;


import java.util.ResourceBundle;

/**
 * Created by yanshi on 9/12/14.
 */
public class Config {

    public static final ResourceBundle CONFIG = ResourceBundle.getBundle("config");

    public static final String DRIVER_CLASS = CONFIG.getString("jdbc.driver");
    public static final String DB_URL = CONFIG.getString("jdbc.url");
    public static final String USER_NAME = CONFIG.getString("jdbc.username");
    public static final String PASS_WORD = CONFIG.getString("jdbc.password");


}
