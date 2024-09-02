package com.Wavemaker.LeaveManagment.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3307/leavemanagment";
    private static final String USER = "root";
    private static final String PASSWORD = "Girish@02";


static {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to load JDBC driver.");
    }

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}