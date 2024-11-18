package com.example.comwanyoikesufeeds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHandler {
    private static final String url = "jdbc:postgresql://localhost:5432/sufeeds";
    private static final String user = "davidmuchiri";
    private static final String password = "Davidwan1*";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }
}
