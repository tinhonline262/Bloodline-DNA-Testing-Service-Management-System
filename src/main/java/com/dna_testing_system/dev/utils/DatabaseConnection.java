package com.dna_testing_system.dev.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/dna_booking_db";
    private static final String USER = "ThuyTien"; // Thay bằng tên người dùng MySQL
    private static final String PASSWORD = "38632347tt@"; // Thay bằng mật khẩu MySQL

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}